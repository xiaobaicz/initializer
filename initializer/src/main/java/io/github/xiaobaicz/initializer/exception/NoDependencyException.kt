package io.github.xiaobaicz.initializer.exception

class NoDependencyException(
    aClass: Class<*>,
    bClass: Class<*>,
) : RuntimeException(
    "${aClass.name} lack of ${bClass.name} dependence"
)