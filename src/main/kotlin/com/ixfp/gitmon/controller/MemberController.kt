package com.ixfp.gitmon.controller

import com.ixfp.gitmon.common.type.ApiErrorType
import com.ixfp.gitmon.common.type.ApiResponse
import com.ixfp.gitmon.config.web.AUTHENTICATED_MEMBER
import com.ixfp.gitmon.controller.request.CreateRepoRequest
import com.ixfp.gitmon.controller.response.CheckRepoNameResponse
import com.ixfp.gitmon.controller.response.GithubRepoUrlResponse
import com.ixfp.gitmon.domain.auth.AuthService
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.member.MemberService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.naming.AuthenticationException

@RestController
@RequestMapping("/api/v1/member")
class MemberController(
    private val authService: AuthService,
    private val memberService: MemberService,
) : IMemberController {
    @PostMapping("/repo")
    override fun upsertRepo(
        @RequestBody request: CreateRepoRequest,
        @RequestAttribute(AUTHENTICATED_MEMBER) member: Member,
    ): ApiResponse<Unit> {
        try {
            val githubAccessToken =
                authService.getGithubAccessToken(member.id)
                    ?: throw AuthenticationException("사용자의 깃허브 토큰을 찾을 수 없음")

            if (member.repoName == request.name) {
                return ApiResponse.success()
            }
            memberService.upsertRepo(member, request.name, githubAccessToken)
            return ApiResponse.success()
        } catch (e: Exception) {
            log.error(e) { "Failed to create repository: ${e.message}" }
            val errorType =
                when (e) {
                    is IllegalArgumentException -> ApiErrorType.BAD_REQUEST
                    is AuthenticationException -> ApiErrorType.UNAUTHORIZED
                    else -> ApiErrorType.INTERNAL_SERVER_ERROR
                }
            return ApiResponse.error(errorType)
        }
    }

    @GetMapping("/repo/check")
    override fun checkRepoName(
        @RequestParam name: String,
        @RequestAttribute(AUTHENTICATED_MEMBER) member: Member,
    ): ApiResponse<CheckRepoNameResponse> {
        return try {
            val isAvailable = memberService.isRepoNameAvailable(member, name)
            ApiResponse.success(CheckRepoNameResponse(isAvailable))
        } catch (e: Exception) {
            log.error(e) { "Failed to check repository name: ${e.message}" }
            val errorType =
                when (e) {
                    is IllegalArgumentException -> ApiErrorType.BAD_REQUEST
                    is AuthenticationException -> ApiErrorType.UNAUTHORIZED
                    else -> ApiErrorType.INTERNAL_SERVER_ERROR
                }
            ApiResponse.error(errorType)
        }
    }

    @GetMapping("/github/repo")
    override fun findGithubRepoUrl(
        @RequestParam githubUsername: String,
    ): ApiResponse<GithubRepoUrlResponse> {
        val githubRepoUrl =
            memberService.findGithubRepoUrl(githubUsername)
                ?: return ApiResponse.success()

        val response = GithubRepoUrlResponse(githubRepoUrl)

        return ApiResponse.success(response)
    }

    companion object {
        private val log = logger {}
    }
}
