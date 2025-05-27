package com.ixfp.gitmon.controller

import com.ixfp.gitmon.controller.response.RefreshResponse
import com.ixfp.gitmon.controller.type.ApiResponseBody
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Auth Token Control", description = "JWT Access/Refresh 토큰 관련 API")
interface IAuthController {
    @Operation(
        summary = "토큰 재발급",
        description = "Refresh Token 쿠키를 이용해 Access Token, Refresh Token 재발급",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "토큰 재발급 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema =
                            Schema(
                                example =
                                    """
                                    {
                                        "status": "SUCCESS",
                                        "data": {
                                            "accessToken": "header.payload.signature"
                                        }
                                    }
                                    """,
                            ),
                    ),
                ],
                headers = [
                    Header(
                        name = "Set-Cookie",
                        description = "새로운 Refresh Token 쿠키(선택)",
                        schema = Schema(type = "string"),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "만료되었거나 유효하지 않은 Refresh Token",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema =
                            Schema(
                                example =
                                    """
                                    {
                                        "status": "UNAUTHORIZED",
                                        "errorMessage": "Invalid or expired refresh token"
                                    }
                                    """,
                            ),
                    ),
                ],
            ),
        ],
    )
    fun refreshToken(refreshToken: String?): ResponseEntity<ApiResponseBody<RefreshResponse>>
}
