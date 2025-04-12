package com.ixfp.gitmon.controller

import com.ixfp.gitmon.config.web.AUTHENTICATED_MEMBER
import com.ixfp.gitmon.controller.request.CreateRepoRequest
import com.ixfp.gitmon.controller.response.GithubRepoUrlResponse
import com.ixfp.gitmon.domain.auth.AuthService
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.member.MemberService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
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

    @GetMapping("/repo/check")
    override fun checkRepoName(
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

    @GetMapping("/github/repo")
    override fun findGithubRepoUrl(
        @RequestParam githubUsername: String,
    ): ResponseEntity<GithubRepoUrlResponse> {
        val githubRepoUrl =
            memberService.findGithubRepoUrl(githubUsername)
                ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        val response = GithubRepoUrlResponse(githubRepoUrl)

        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    companion object {
        private val log = logger {}
    }
}
