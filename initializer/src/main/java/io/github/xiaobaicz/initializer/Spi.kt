package io.github.xiaobaicz.initializer

import java.util.ServiceLoader

internal inline fun <reified T : Any> loadSpi(): List<T> {
    val spiClass = T::class.java
    return try {
        ServiceLoader.load(spiClass, spiClass.classLoader).toList()
    } catch (t: Throwable) {
        emptyList()
    }
}