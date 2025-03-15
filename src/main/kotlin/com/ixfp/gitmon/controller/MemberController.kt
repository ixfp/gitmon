package com.ixfp.gitmon.controller

import com.ixfp.gitmon.common.const.AUTHENTICATED_MEMBER
import com.ixfp.gitmon.controller.request.CreateRepoRequest
import com.ixfp.gitmon.domain.auth.AuthService
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.member.MemberService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.naming.AuthenticationException

@RequestMapping("/api/v1/member")
@RestController()
@Tag(name = "Member", description = "회원 관련 API")
class MemberController(
    private val authService: AuthService,
    private val memberService: MemberService,
) {
    @Operation(
        summary = "레포지토리 생성/갱신",
        description = "이미 설정된 레포지토리가 있다면 갱신하고, 없다면 새로 생성합니다.",
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
    @PostMapping("/repo")
    fun upsertRepo(
        @RequestBody request: CreateRepoRequest,
        @RequestAttribute(AUTHENTICATED_MEMBER) member: Member,
    ): ResponseEntity<HttpStatus> {
        try {
            val githubAccessToken =
                authService.getGithubAccessToken(member.id)
                    ?: throw AuthenticationException("사용자의 깃허브 토큰을 찾을 수 없음")

            if (member.repoName == request.name) {
                return ResponseEntity(HttpStatus.OK)
            }
            memberService.upsertRepo(member, request.name, githubAccessToken)
            return ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (e: Exception) {
            log.error(e) { "Failed to create repository: ${e.message}" }
            val status =
                when (e) {
                    is IllegalArgumentException -> HttpStatus.BAD_REQUEST
                    is AuthenticationException -> HttpStatus.UNAUTHORIZED
                    else -> HttpStatus.INTERNAL_SERVER_ERROR
                }
            return ResponseEntity.status(status).build()
        }
    }

    @Operation(
        summary = "레포지토리 이름 중복 조회",
        description = "입력한 레포지토리 이름이 이미 사용 중인지 확인합니다.",
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
    @GetMapping("/repo/check")
    fun checkRepoName(
        @RequestParam name: String,
        @RequestAttribute(AUTHENTICATED_MEMBER) member: Member,
    ): ResponseEntity<Unit> {
        return try {
            val isAvailable = memberService.isRepoNameAvailable(member, name)
            if (!isAvailable) {
                ResponseEntity.status(HttpStatus.CONFLICT).build()
            } else {
                ResponseEntity.ok().build()
            }
        } catch (e: Exception) {
            log.error(e) { "Failed to check repository name: ${e.message}" }
            val status =
                when (e) {
                    is IllegalArgumentException -> HttpStatus.BAD_REQUEST
                    is AuthenticationException -> HttpStatus.UNAUTHORIZED
                    else -> HttpStatus.INTERNAL_SERVER_ERROR
                }
            ResponseEntity.status(status).build()
        }
    }

    companion object {
        private val log = logger {}
    }
}
