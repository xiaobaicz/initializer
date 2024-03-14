package io.github.xiaobaicz.initializer

class NoDependencyException(
    aClass: Class<*>,
    bClass: Class<*>,
) : RuntimeException(
    "${aClass.name} lack of ${bClass.name} dependence"
)