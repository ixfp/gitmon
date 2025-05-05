package com.ixfp.gitmon.common.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Aspect
@Component
class ExceptionWrappingAspect(
    private val applicationContext: ApplicationContext,
) {
    @Around("@annotation(wrapWith)")
    fun aroundAnnotated(
        joinPoint: ProceedingJoinPoint,
        wrapWith: WrapWith,
    ): Any? {
        return try {
            joinPoint.proceed()
        } catch (original: Throwable) {
            val strategyClass = wrapWith.value.java
            val strategy = applicationContext.getBean(strategyClass)
            throw strategy.wrap(original)
        }
    }
}
