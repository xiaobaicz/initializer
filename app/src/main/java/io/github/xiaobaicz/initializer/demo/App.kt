package io.github.xiaobaicz.initializer.demo

import android.app.Application
import io.github.xiaobaicz.initializer.Initializer

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Initializer.init(this)
    }

}