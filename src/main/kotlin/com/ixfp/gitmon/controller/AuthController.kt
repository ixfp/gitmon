package com.ixfp.gitmon.controller

import com.ixfp.gitmon.common.util.JwtUtil
import com.ixfp.gitmon.controller.response.RefreshResponse
import com.ixfp.gitmon.controller.type.ApiError
import com.ixfp.gitmon.controller.type.ApiResponseBody
import com.ixfp.gitmon.controller.util.ApiResponseHelper
import com.ixfp.gitmon.controller.util.withCookie
import com.ixfp.gitmon.domain.auth.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
@RequestMapping("/api/v1")
class AuthController(
    private val authService: AuthService,
    private val jwtUtil: JwtUtil,
) : IAuthController {
    @PostMapping("/refresh")
    override fun refreshToken(
        @CookieValue("refreshToken") refreshToken: String?,
    ): ResponseEntity<ApiResponseBody<RefreshResponse>> {
        println(refreshToken)
        if (refreshToken.isNullOrBlank()) {
            return ApiResponseHelper.error(ApiError.INVALID_REFRESH_TOKEN)
        }

        val exposedId =
            jwtUtil.parseAccessToken(refreshToken)?.exposedId
                ?: run {
                    return ApiResponseHelper.error(ApiError.INVALID_REFRESH_TOKEN)
                }

        val member =
            authService.getMemberByExposedId(exposedId)
                ?: run {
                    return ApiResponseHelper.error(ApiError.INVALID_REFRESH_TOKEN)
                }

        val newAccessToken = authService.createAccessToken(member)
        val newRefreshToken = authService.createRefreshToken(member)

        return ApiResponseHelper.success(RefreshResponse(newAccessToken))
            .withCookie(
                key = "refreshToken",
                value = newRefreshToken,
                maxAge = Duration.ofDays(30),
                path = "/api/v1/refresh",
                httpOnly = true,
                secure = true,
                sameSite = "Lax",
            )
    }
}
