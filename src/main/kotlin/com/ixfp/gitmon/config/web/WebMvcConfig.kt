package com.ixfp.gitmon.config.web

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
            .addPathPatterns("/api/v1/member/*")
            .addPathPatterns("/api/v1/posting", "/api/v1/posting/")
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*ALLOWED_ORIGINS)
            .allowedMethods(*ALLOWED_METHODS)
            .allowedHeaders("*")
            .allowCredentials(true)
    }

    companion object {
        private const val GITMON_CLIENT_URL = "https://gitmon.blog"
        private const val GITMON_API_URL = "https://api.gitmon.blog"
        private val ALLOWED_ORIGINS = arrayOf(GITMON_CLIENT_URL, GITMON_API_URL, "http://localhost:3000", "http://127.0.0.1:3000")
        private val ALLOWED_METHODS = arrayOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
    }
}
