package com.ixfp.gitmon.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val jwtAuthInterceptor: JwtAuthInterceptor,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(jwtAuthInterceptor)
            .addPathPatterns("/api/v1/member/repo/**") // 인증이 필요한 경로
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*ALLOWED_ORIGINS)
            .allowedMethods(*ALLOWED_METHODS)
            .allowedHeaders("*")
            .allowCredentials(true)
    }

    companion object {
        private val GITMON_CLIENT_URL = "https://gitmon.blog"
        private val ALLOWED_ORIGINS = arrayOf(GITMON_CLIENT_URL)
        private val ALLOWED_METHODS = arrayOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
    }
}
