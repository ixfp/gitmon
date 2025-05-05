package com.ixfp.gitmon.common.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import kotlin.reflect.full.findAnnotation

@Aspect
@Component
class ExceptionWrappingAspect(
    private val applicationContext: ApplicationContext,
) {
    @Around(
        "execution(public * *(..)) && " +
            "(@annotation(wrapWith) || @within(wrapWith))",
    )
    fun aroundAnnotated(
        joinPoint: ProceedingJoinPoint,
        wrapWith: WrapWith?,
    ): Any? {
        val ann =
            wrapWith
                ?: joinPoint.signature.declaringType.kotlin
                    .findAnnotation<WrapWith>()
                ?: return joinPoint.proceed()

        return try {
            joinPoint.proceed()
        } catch (original: Throwable) {
            val strategy = applicationContext.getBean(ann.value.java)
            throw strategy.wrap(original)
        }
    }
}
