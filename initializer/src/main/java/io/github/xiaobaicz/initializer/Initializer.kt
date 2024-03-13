package io.github.xiaobaicz.initializer

import android.content.Context
import io.github.xiaobaicz.initializer.exception.CircularDependencyException
import io.github.xiaobaicz.initializer.exception.NoDependencyException
import io.github.xiaobaicz.initializer.utils.loadSpi

interface Initializer {

    fun onInit(context: Context)

    fun dependencies(): Set<Class<out Initializer>> = emptySet()

    companion object {
        @JvmStatic
        fun init(context: Context) {
            val initSet = HashSet<Class<out Initializer>>()
            val waitSet = HashSet<Class<out Initializer>>()
            val waitList = ArrayList<Initializer>()
            loadSpi<Initializer>().forEach {
                if (it.dependencies().isEmpty()) {
                    it.onInit(context)
                    initSet.add(it::class.java)
                    return@forEach
                }
                waitList.add(it)
                waitSet.add(it::class.java)
            }

            while (waitList.isNotEmpty()) {
                // 每一轮初始化计数器，为 0 则存在循环依赖
                var isCircular = true
                for (it in waitList) {
                    var count = 0
                    val dependencies = it.dependencies()
                    for (clazz in dependencies) {
                        when {
                            initSet.contains(clazz) -> count++
                            // 缺少依赖
                            !waitSet.contains(clazz) -> throw NoDependencyException(it::class.java, clazz)
                        }
                    }
                    // 依赖完整，初始化并进入下一轮
                    if (count == dependencies.size) {
                        isCircular = false
                        it.onInit(context)
                        initSet.add(it::class.java)
                        waitList.remove(it)
                        waitSet.remove(it::class.java)
                        break
                    }
                }
                // 存在循环依赖
                if (isCircular) {
                    val it = waitList[0]
                    val clazz = it.dependencies().first()
                    throw CircularDependencyException(it::class.java, clazz)
                }
            }
        }
    }

}
