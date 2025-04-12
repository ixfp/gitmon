package com.ixfp.gitmon.controller

import com.ixfp.gitmon.config.docs.ACCESS_TOKEN
import com.ixfp.gitmon.controller.request.CreateRepoRequest
import com.ixfp.gitmon.controller.response.GithubRepoUrlResponse
import com.ixfp.gitmon.domain.member.Member
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@Tag(name = "Member", description = "회원 관련 API")
interface IMemberController {
    @Operation(
        summary = "레포지토리 생성/갱신",
        description = "이미 설정된 레포지토리가 있다면 갱신하고, 없다면 새로 생성합니다.",
        security = [SecurityRequirement(name = ACCESS_TOKEN)],
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "레포 생성/갱신 성공",
            ),
            ApiResponse(
                responseCode = "200",
                description = "레포지토리 이름이 설정된 사용자 레포 이름과 동일함",
            ),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (예: 파라미터 오류 등)",
            ),
            ApiResponse(
                responseCode = "401",
                description = "인증 실패 (엑세스 토큰 문제)",
            ),
            ApiResponse(
                responseCode = "500",
                description = "서버 내부 오류",
            ),
        ],
    )
    fun upsertRepo(
        request: CreateRepoRequest,
        member: Member,
    ): ResponseEntity<HttpStatus>

    @Operation(
        summary = "레포지토리 이름 중복 조회",
        description = "입력한 레포지토리 이름이 이미 사용 중인지 확인합니다.",
        security = [SecurityRequirement(name = ACCESS_TOKEN)],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "사용 가능한 레포지토리 이름"),
            ApiResponse(responseCode = "409", description = "이미 사용 중인 레포지토리 이름"),
            ApiResponse(responseCode = "401", description = "인증 실패 (엑세스 토큰 문제)"),
            ApiResponse(responseCode = "400", description = "잘못된 요청"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류"),
        ],
    )
    fun checkRepoName(
        name: String,
        member: Member,
    ): ResponseEntity<Unit>

    @Operation(summary = "레포지토리 URL 조회")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "404", description = "레포지토리 URL 찾을 수 없음"),
        ],
    )
    fun findGithubRepoUrl(githubUsername: String): ResponseEntity<GithubRepoUrlResponse>
}
