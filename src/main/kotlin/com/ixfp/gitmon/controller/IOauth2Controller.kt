package com.ixfp.gitmon.controller

import com.ixfp.gitmon.controller.request.GithubOauth2Request
import com.ixfp.gitmon.controller.response.AccessTokenResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

@Tag(name = "OAuth Token Control", description = "OAuth 인증 관련 API")
interface IOauth2Controller {
    @Operation(summary = "Get Oauth2 Token", description = "Github으로부터 Oauth2 Token을 받아오는 API")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "301",
                description = "Success",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = ArraySchema(schema = Schema(implementation = String::class)),
                    ),
                ],
            ),
        ],
    )
    fun redirectToGithubOauthUrl(): ResponseEntity<Unit>

    @Operation(
        summary = "gitmon Access 토큰 발급",
        description = "Github OAuth 인증 코드를 받아 로그인 처리 후 gitmon의 Access Token을 발급 받음.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "로그인 및 토큰 발급 성공",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = AccessTokenResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (code가 유효하지 않은 경우 등)",
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증 실패",
            ),
            ApiResponse(
                responseCode = "500",
                description = "서버 에러",
            ),
        ],
    )
    fun login(request: GithubOauth2Request): ResponseEntity<AccessTokenResponse>
}
