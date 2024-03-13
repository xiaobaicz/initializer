package io.github.xiaobaicz.initializer.exception

class CircularDependencyException(
    aClass: Class<*>,
    bClass: Class<*>,
) : RuntimeException(
    "${aClass.name} and ${bClass.name} have circular dependencies"
)