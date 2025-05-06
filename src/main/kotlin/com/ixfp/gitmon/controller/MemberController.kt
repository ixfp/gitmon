package com.ixfp.gitmon.controller

import com.ixfp.gitmon.config.web.AUTHENTICATED_MEMBER
import com.ixfp.gitmon.controller.request.CreateRepoRequest
import com.ixfp.gitmon.controller.response.CheckRepoNameResponse
import com.ixfp.gitmon.controller.response.GithubRepoUrlResponse
import com.ixfp.gitmon.controller.response.MemberInfoResponse
import com.ixfp.gitmon.controller.type.ApiResponseBody
import com.ixfp.gitmon.controller.util.ApiResponseHelper
import com.ixfp.gitmon.domain.auth.AuthService
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.member.MemberService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.http.ResponseEntity
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
    @GetMapping
    override fun getMember(
        @RequestAttribute(AUTHENTICATED_MEMBER) member: Member,
    ): ResponseEntity<ApiResponseBody<MemberInfoResponse>> {
        val response =
            MemberInfoResponse(
                id = member.exposedId,
                githubUsername = member.githubUsername,
                repoName = member.repoName,
                isRepoCreated = member.repoName != null,
            )
        return ApiResponseHelper.success(response)
    }

    @PostMapping("/repo")
    override fun upsertRepo(
        @RequestBody request: CreateRepoRequest,
        @RequestAttribute(AUTHENTICATED_MEMBER) member: Member,
    ): ResponseEntity<ApiResponseBody<Unit>> {
        val githubAccessToken =
            authService.getGithubAccessToken(member.id)
                ?: throw AuthenticationException("사용자의 깃허브 토큰을 찾을 수 없음")

        if (member.repoName == request.name) {
            return ApiResponseHelper.success()
        }
        memberService.upsertRepo(member, request.name, githubAccessToken)
        return ApiResponseHelper.success()
    }

    @GetMapping("/repo/check")
    override fun checkRepoName(
        @RequestParam name: String,
        @RequestAttribute(AUTHENTICATED_MEMBER) member: Member,
    ): ResponseEntity<ApiResponseBody<CheckRepoNameResponse>> {
        val isAvailable = memberService.isRepoNameAvailable(member, name)
        return ApiResponseHelper.success(CheckRepoNameResponse(isAvailable))
    }

    @GetMapping("/github/repo")
    override fun findGithubRepoUrl(
        @RequestParam githubUsername: String,
    ): ResponseEntity<ApiResponseBody<GithubRepoUrlResponse>> {
        val githubRepoUrl =
            memberService.findGithubRepoUrl(githubUsername)
                ?: return ApiResponseHelper.success()

        val response = GithubRepoUrlResponse(githubRepoUrl)
        return ApiResponseHelper.success(response)
    }

    companion object {
        private val log = logger {}
    }
}
