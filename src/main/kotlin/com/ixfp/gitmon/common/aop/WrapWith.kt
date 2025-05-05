package com.ixfp.gitmon.common.aop

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class WrapWith(
    val value: KClass<out ExceptionWrappingStrategy>,
)
