package com.ixfp.gitmon.controller

import com.ixfp.gitmon.client.github.GithubApiService
import com.ixfp.gitmon.common.type.Profile
import com.ixfp.gitmon.controller.request.GithubOauth2Request
import com.ixfp.gitmon.controller.response.AccessTokenResponse
import com.ixfp.gitmon.controller.type.ApiResponseBody
import com.ixfp.gitmon.controller.util.ApiResponseHelper
import com.ixfp.gitmon.domain.auth.AuthService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class Oauth2Controller(
    private val authService: AuthService,
    private val githubApiService: GithubApiService,
) : IOauth2Controller {
    @GetMapping("/login/oauth/github")
    override fun redirectToGithubOauthUrl(profile: String?): ResponseEntity<Unit> {
        val redirectionUrl = githubApiService.getAuthRedirectionUrl(Profile.findByCode(profile))
        return ApiResponseHelper.redirectPermanent(redirectionUrl)
    }

    @PostMapping("/login/oauth/github/tokens")
    override fun login(
        @RequestParam profile: String?,
        @RequestBody request: GithubOauth2Request,
    ): ResponseEntity<ApiResponseBody<AccessTokenResponse>> {
        val githubAccessToken = githubApiService.getAccessTokenByCode(request.code, Profile.findByCode(profile))
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
        return ApiResponseHelper.success(response)
    }

    companion object {
        private val log = logger {}
    }
}
