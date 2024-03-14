### Android 初始化器

- 支持多进程
- 支持顺序初始化(PS：通过依赖项)

#### 使用方法
~~~ kts
plugins {
    id("kotlin-kapt")
}
dependencies {
    implementation("io.github.xiaobaicz:initializer:0.0.1")
    implementation("com.google.auto.service:auto-service-annotations:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
}
~~~

~~~ kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Initializer.init(this)
    }
}

@AutoService(Initializer::class)
class XxxInit : Initializer {
    override fun onInit(context: Context) {
        println(this)
    }
    // 可选方法，依赖集合，优先初始化依赖项
    override fun dependencies(): Set<Class<out Initializer>> {
        return setOf(Xxx1Init::class.java, Xxx2Init::class.java)
    }
}
~~~