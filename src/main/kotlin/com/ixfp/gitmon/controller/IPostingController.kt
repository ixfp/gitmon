package com.ixfp.gitmon.controller

import com.ixfp.gitmon.config.docs.ACCESS_TOKEN
import com.ixfp.gitmon.controller.type.ApiResponseBody
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.posting.PostingReadDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Posting", description = "게시글(포스팅) 관련 API")
interface IPostingController {
    @Operation(
        summary = "포스팅 생성",
        description = "포스팅을 생성합니다.",
        security = [SecurityRequirement(name = ACCESS_TOKEN)],
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "포스팅 생성 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema =
                            Schema(
                                example = """
                                {
                                    "status": "CREATED",
                                    "data": null
                                }
                            """,
                            ),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "허용되지 않은 이미지 확장자",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema =
                            Schema(
                                example = """
                                {
                                    "status": "BAD_REQUEST",
                                    "errorMessage": "invalid image extension"
                                }
                            """,
                            ),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "409",
                description = "사용자의 레포지토리가 아직 설정되지 않음",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema =
                            Schema(
                                example = """
                                {
                                    "status": "CONFLICT",
                                    "errorMessage": "repository not configured"
                                }
                            """,
                            ),
                    ),
                ],
            ),
        ],
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "포스팅 생성 요청 (제목 + 파일)",
        required = true,
        content = [
            Content(
                mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
            ),
        ],
    )
    fun createPosting(
        title: String,
        content: MultipartFile,
        member: Member,
    ): ResponseEntity<ApiResponseBody<PostingReadDto>>

    fun getPostingList(exposedMemberId: String): ResponseEntity<ApiResponseBody<List<PostingReadDto>>>

    fun getPostingListByGithubUsername(githubUsername: String): ResponseEntity<ApiResponseBody<List<PostingReadDto>>>

    fun getPosting(
        githubUsername: String,
        postingId: Long,
    ): ResponseEntity<ApiResponseBody<PostingReadDto>>

    fun updatePosting(
        id: Long,
        title: String,
        content: MultipartFile,
        member: Member,
    ): ResponseEntity<ApiResponseBody<PostingReadDto>>

    @Operation(
        summary = "이미지 업로드",
        description = "포스팅에 사용할 이미지를 업로드합니다.",
        security = [SecurityRequirement(name = ACCESS_TOKEN)],
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "이미지 업로드 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema =
                            Schema(
                                example = """
                                {
                                    "status": "CREATED",
                                    "data": "https://raw.githubusercontent.com/user/repo/main/images/uuid.jpg"
                                }
                            """,
                            ),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "허용되지 않은 이미지 확장자",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema =
                            Schema(
                                example = """
                                {
                                    "status": "BAD_REQUEST",
                                    "errorMessage": "invalid image extension"
                                }
                            """,
                            ),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "409",
                description = "사용자의 레포지토리가 아직 설정되지 않음",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema =
                            Schema(
                                example = """
                                {
                                    "status": "CONFLICT",
                                    "errorMessage": "repository not configured"
                                }
                            """,
                            ),
                    ),
                ],
            ),
        ],
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "업로드할 이미지 파일",
        required = true,
        content = [
            Content(
                mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
            ),
        ],
    )
    fun uploadImage(
        image: MultipartFile,
        member: Member,
    ): ResponseEntity<ApiResponseBody<String>>
}
