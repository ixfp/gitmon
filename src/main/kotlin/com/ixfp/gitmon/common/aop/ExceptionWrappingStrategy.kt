package com.ixfp.gitmon.common.aop

import com.ixfp.gitmon.common.exception.AppException

interface ExceptionWrappingStrategy {
    fun wrap(e: Throwable): AppException
}
