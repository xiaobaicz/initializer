package io.github.xiaobaicz.initializer

import android.content.Context

/**
 * 初始化器
 *
 * ~~~ Kotlin
 * @AutoService(Initializer::class)
 * class XxxInit : Initializer {
 *     override fun onInit(context: Context) {
 *         println(this)
 *     }
 *
 *     // 可选方法，依赖集合，优先初始化依赖项
 *     override fun dependencies(): Set<Class<out Initializer>> {
 *         return setOf(Xxx1Init::class.java, Xxx2Init::class.java)
 *     }
 * }
 * ~~~
 */
interface Initializer {

    /**
     * 初始化
     * @param context Application Context
     */
    fun onInit(context: Context)

    /**
     * 可选方法，依赖集合，优先初始化依赖项，默认无依赖项
     * @return 依赖集合
     */
    fun dependencies(): Set<Class<out Initializer>> = emptySet()

    companion object {
        // 是否已初始化
        private var init = false

        /**
         * 初始化，需要在[android.app.Application.onCreate]处调用此方法
         * @exception NoDependencyException 没有找到依赖项
         * @exception CircularDependencyException 存在循环依赖
         */
        @JvmStatic
        fun init(context: Context) {
            if (init) return
            init = true

            // 已初始化的类
            val initSet = HashSet<Class<out Initializer>>()
            // 等待初始化的类
            val waitSet = HashSet<Class<out Initializer>>()
            // 等待初始化的对象
            val waitList = ArrayList<Initializer>()
            // 依赖项缓存
            val dependenciesCache = HashMap<Initializer, Set<Class<out Initializer>>>()

            // 初始化，并记录
            val initAndRecord = { it: Initializer ->
                it.onInit(context)
                initSet.add(it::class.java)
            }

            // 等待初始化，并记录
            val waitAndRecord = { it: Initializer, dependencies: Set<Class<out Initializer>> ->
                waitList.add(it)
                waitSet.add(it::class.java)
                dependenciesCache[it] = dependencies
            }

            // 首轮初始化不需要依赖的初始化器
            loadSpi<Initializer>().forEach {
                val dependencies = it.dependencies()
                if (dependencies.isEmpty())
                    initAndRecord(it)
                else
                    waitAndRecord(it, dependencies)
            }

            // 处理等待依赖项，处理完成 or 出现异常
            while (waitList.isNotEmpty()) {
                // 每一轮循环依赖标识，true为循环依赖
                var isCircular = true

                val iterator = waitList.iterator()
                while (iterator.hasNext()) {
                    val it = iterator.next()
                    var count = 0
                    val dependencies = dependenciesCache[it]!!
                    // 检查依赖项
                    for (clazz in dependencies) {
                        when {
                            initSet.contains(clazz) -> count++
                            !waitSet.contains(clazz) -> noDependency(it, clazz)
                        }
                    }
                    // 依赖完整，初始化并进入下一轮
                    if (count == dependencies.size) {
                        isCircular = false
                        initAndRecord(it)
                        // 清除等待记录
                        iterator.remove()
                        waitSet.remove(it::class.java)
                        break
                    }
                }
                // 存在循环依赖
                if (isCircular) {
                    val first = waitList[0]
                    val clazz = dependenciesCache[first]!!.first()
                    circularDependency(first, clazz)
                }
            }
        }

        private fun noDependency(it: Initializer, clazz: Class<out Initializer>): Nothing {
            throw NoDependencyException(it::class.java, clazz)
        }

        private fun circularDependency(it: Initializer, clazz: Class<out Initializer>): Nothing {
            throw CircularDependencyException(it::class.java, clazz)
        }

    }

}
