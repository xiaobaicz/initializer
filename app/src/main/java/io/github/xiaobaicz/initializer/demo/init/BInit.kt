package io.github.xiaobaicz.initializer.demo.init

import android.content.Context
import com.google.auto.service.AutoService
import io.github.xiaobaicz.initializer.Initializer

@AutoService(Initializer::class)
class BInit : Initializer {
    override fun onInit(context: Context) {
        println(this)
    }

    override fun dependencies(): Set<Class<out Initializer>> {
        return setOf(CInit::class.java)
    }
}