package com.ixfp.gitmon.controller

import com.ixfp.gitmon.client.github.GithubApiService
import com.ixfp.gitmon.controller.request.GithubOauth2Request
import com.ixfp.gitmon.controller.response.AccessTokenResponse
import com.ixfp.gitmon.domain.auth.AuthService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.naming.AuthenticationException

@RestController
@RequestMapping("/api/v1")
class Oauth2Controller(
    @Value("\${oauth2.client.github.id}") private val githubClientId: String,
    private val authService: AuthService,
    private val githubApiService: GithubApiService,
) : IOauth2Controller {
    @GetMapping("/login/oauth/github")
    override fun redirectToGithubOauthUrl(): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(
            "Location",
            "https://github.com/login/oauth/authorize?&scope=repo&client_id=$githubClientId",
        ).build()
    }

    @PostMapping("/login/oauth/github/tokens")
    override fun login(
        @RequestBody request: GithubOauth2Request,
    ): ResponseEntity<AccessTokenResponse> {
        try {
            val githubAccessToken = githubApiService.getAccessTokenByCode(request.code)
            val githubUser = githubApiService.getGithubUser(githubAccessToken)
            val member =
                authService.getMemberByGithubId(githubUser.id.toLong())
                    ?: authService.signup(githubAccessToken, githubUser)

            authService.login(githubAccessToken, member)

            val accessToken = authService.createAccessToken(member)
            val isRepoCreated = member.repoName != null

            val response =
                AccessTokenResponse(
                    id = member.exposedId,
                    accessToken = accessToken,
                    isRepoCreated = isRepoCreated,
                )
            return ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: Exception) {
            log.error(e) { "Failed to login: ${e.message}" }
            val status =
                when (e) {
                    is IllegalArgumentException -> HttpStatus.BAD_REQUEST
                    is AuthenticationException -> HttpStatus.UNAUTHORIZED
                    else -> HttpStatus.INTERNAL_SERVER_ERROR
                }
            return ResponseEntity.status(status).build()
        }
    }

    companion object {
        private val log = logger {}
    }
}
