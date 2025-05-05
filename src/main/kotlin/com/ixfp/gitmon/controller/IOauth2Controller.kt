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
    fun redirectToGithubOauthUrl(profile: String?): ResponseEntity<Unit>

    @Operation(
        summary = "gitmon Access 토큰 발급",
        description = "Github OAuth 인증 코드를 받아 로그인 처리 후 gitmon의 Access Token을 발급 받음.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
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
            ),
            // TODO: 깃허브 API 에러로 인한 에러 응답 예제 추가
        ],
    )
    fun login(
        profile: String?,
        request: GithubOauth2Request,
    ): com.ixfp.gitmon.common.type.ApiResponseBody<AccessTokenResponse>
}
