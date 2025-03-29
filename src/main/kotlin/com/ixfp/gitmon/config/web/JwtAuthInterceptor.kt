package com.ixfp.gitmon.config.web

import com.ixfp.gitmon.common.util.JwtUtil
import com.ixfp.gitmon.domain.auth.AuthService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class JwtAuthInterceptor(
    private val jwtUtil: JwtUtil,
    private val authService: AuthService,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (request.method.equals("OPTIONS", ignoreCase = true)) {
            return true
        }

        val authHeader = request.getHeader("Authorization")

        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header")
            return false
        }

        val token = authHeader.removePrefix("Bearer ")
        val exposedId =
            jwtUtil.parseAccessToken(token)?.exposedId
                ?: run {
                    log.info { "유효하지 않은 사용자 토큰" }
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token")
                    return false
                }

        val member =
            authService.getMemberByExposedId(exposedId)
                ?: run {
                    log.info { "토큰에 해당하는 사용자를 찾을 수 없음" }
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token")
                    return false
                }

        request.setAttribute(AUTHENTICATED_MEMBER, member) // 요청 속성에 저장하여 컨트롤러에서 사용 가능
        return true
    }

    companion object {
        private val log = logger {}
    }
}
