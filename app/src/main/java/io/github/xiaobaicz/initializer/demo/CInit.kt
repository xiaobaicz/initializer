package io.github.xiaobaicz.initializer.demo

import android.content.Context
import com.google.auto.service.AutoService
import io.github.xiaobaicz.initializer.Initializer

@AutoService(Initializer::class)
class CInit : Initializer {
    override fun onInit(context: Context) {
        println(this)
    }
}