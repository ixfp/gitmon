package com.ixfp.gitmon.controller

import com.ixfp.gitmon.config.docs.ACCESS_TOKEN
import com.ixfp.gitmon.controller.request.CreateRepoRequest
import com.ixfp.gitmon.controller.response.CheckRepoNameResponse
import com.ixfp.gitmon.controller.response.GithubRepoUrlResponse
import com.ixfp.gitmon.controller.response.MemberInfoResponse
import com.ixfp.gitmon.domain.member.Member
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Member", description = "회원 관련 API")
interface IMemberController {
    @Operation(
        summary = "회원 정보 조회",
        security = [SecurityRequirement(name = ACCESS_TOKEN)],
    )
    @ApiResponses(
        value = [
            ApiResponse(
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
                                            "id": "exposedId",
                                            "githubUsername": "kimsj-git",
                                            "isRepoCreated": "true",
                                            "repoName": "gitmon"
                                        }
                                    }
                                    """,
                            ),
                    ),
                ],
            ),
        ],
    )
    fun getMember(member: Member): com.ixfp.gitmon.common.type.ApiResponse<MemberInfoResponse>

    @Operation(
        summary = "레포지토리 생성/갱신",
        description = "이미 설정된 레포지토리가 있다면 갱신하고, 없다면 새로 생성합니다.",
        security = [SecurityRequirement(name = ACCESS_TOKEN)],
    )
    @ApiResponses(
        value = [
            ApiResponse(
                description = "레포 생성/갱신 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema =
                            Schema(
                                example =
                                    """
                                    {
                                        "status": "SUCCESS",
                                        "data": null
                                    }
                                    """,
                            ),
                    ),
                ],
            ),
        ],
    )
    fun upsertRepo(
        request: CreateRepoRequest,
        member: Member,
    ): com.ixfp.gitmon.common.type.ApiResponse<Unit>

    @Operation(
        summary = "레포지토리 이름 중복 조회",
        description = "입력한 레포지토리 이름을 사용할 수 있는지 확인합니다.",
        security = [SecurityRequirement(name = ACCESS_TOKEN)],
    )
    @ApiResponses(
        value = [
            ApiResponse(
                description = "사용 가능한 레포지토리 이름인지 확인",
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
                                            "isAvailable": true
                                        }
                                    }
                                    """,
                            ),
                    ),
                ],
            ),
        ],
    )
    fun checkRepoName(
        name: String,
        member: Member,
    ): com.ixfp.gitmon.common.type.ApiResponse<CheckRepoNameResponse>

    @Operation(summary = "레포지토리 URL 조회")
    @ApiResponses(
        value = [
            ApiResponse(
                description = "레포지토리 URL 조회",
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
                                            "githubRepoUrl": "https://api.gitmon.blog/api/v1/member/github/repo?githubUsername=kimsj-git"
                                        }
                                    }
                                    """,
                            ),
                    ),
                ],
            ),
        ],
    )
    fun findGithubRepoUrl(githubUsername: String): com.ixfp.gitmon.common.type.ApiResponse<GithubRepoUrlResponse>
}
