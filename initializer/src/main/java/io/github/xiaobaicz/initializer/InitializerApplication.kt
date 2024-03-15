package io.github.xiaobaicz.initializer

import android.app.Application

/**
 * 初始化器Application，理论上App不需要再定义Application
 */
class InitializerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Initializer.init(this)
    }
}