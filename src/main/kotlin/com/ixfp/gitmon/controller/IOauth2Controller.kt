package com.ixfp.gitmon.controller

import com.ixfp.gitmon.controller.request.GithubOauth2Request
import com.ixfp.gitmon.controller.response.LoginResponse
import com.ixfp.gitmon.controller.type.ApiResponseBody
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity

@Tag(name = "OAuth Token Control", description = "OAuth 인증 관련 API")
interface IOauth2Controller {
    @Operation(summary = "Get Oauth2 Token", description = "Github으로부터 Oauth2 Token을 받아오는 API")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "301",
                description = "Redirect to GitHub OAuth page",
                headers = [
                    Header(
                        name = HttpHeaders.LOCATION,
                        description = "GitHub OAuth authorization URL",
                        schema = Schema(type = "string", format = "uri"),
                    ),
                ],
            ),
        ],
    )
    fun redirectToGithubOauthUrl(profile: String?): ResponseEntity<Unit>

    @Operation(
        summary = "gitmon Access, Refresh 토큰 발급",
        description = "Github OAuth 인증 코드를 받아 로그인 처리 후 gitmon의 Access Token을 HTTP Body로 반환 및 Refresh Token 쿠키 설정",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "로그인 및 토큰 발급 성공",
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
                        description = "Refresh token cookie",
                        schema =
                            Schema(
                                type = "string",
                                example = "refreshToken=<KEY>; HttpOnly; Path=/api/v1/refresh; Secure",
                            ),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "유효하지 않은 GitHub 인증 코드",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema =
                            Schema(
                                example = """
                            {
                                "status": "UNAUTHORIZED",
                                "errorMessage": "Invalid auth code"
                            }
                            """,
                            ),
                    ),
                ],
            ),
        ],
    )
    fun login(
        profile: String?,
        request: GithubOauth2Request,
    ): ResponseEntity<ApiResponseBody<LoginResponse>>
}
