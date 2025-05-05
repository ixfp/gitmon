package com.ixfp.gitmon.common.aop

interface ExceptionWrappingStrategy {
    fun wrap(e: Throwable): RuntimeException
}
