package com.ixfp.gitmon.client.github

import com.ixfp.gitmon.client.github.exception.GithubApiExceptionStrategy
import com.ixfp.gitmon.client.github.exception.GithubInvalidAuthCodeException
import com.ixfp.gitmon.client.github.request.GithubAccessTokenRequest
import com.ixfp.gitmon.client.github.request.GithubCreateRepositoryRequest
import com.ixfp.gitmon.client.github.request.GithubUpsertFileRequest
import com.ixfp.gitmon.client.github.response.GithubContent
import com.ixfp.gitmon.client.github.response.GithubUserResponse
import com.ixfp.gitmon.common.aop.WrapWith
import com.ixfp.gitmon.common.type.Profile
import com.ixfp.gitmon.common.util.Base64Encoder
import com.ixfp.gitmon.common.util.BearerToken
import feign.FeignException
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@WrapWith(GithubApiExceptionStrategy::class)
@Component
class GithubApiService(
    private val githubOauth2ApiClient: GithubOauth2ApiClient,
    private val githubResourceApiClient: GithubResourceApiClient,
    private val githubProdClient: GithubOauth2ClientProdProperties,
    private val githubDevClient: GithubOauth2ClientDevProperties,
) {
    fun getGithubUser(githubAccessToken: String): GithubUserResponse {
        return githubResourceApiClient.fetchUser(BearerToken(githubAccessToken).format())
    }

    fun getAuthRedirectionUrl(profile: Profile): String {
        return when (profile) {
            Profile.PROD -> buildAuthRedirectionUrl(githubProdClient.id)
            Profile.DEV -> buildAuthRedirectionUrl(githubDevClient.id)
        }
    }

    fun getAccessTokenByCode(
        code: String,
        profile: Profile,
    ): String {
        val request =
            GithubAccessTokenRequest(
                code = code,
                client_id = githubClientId(profile),
                client_secret = githubClientSecret(profile),
            )

        val response = githubOauth2ApiClient.fetchAccessToken(request)
        if (response.accessToken == null) {
            throw GithubInvalidAuthCodeException("response=$response")
        }
        return response.accessToken
    }

    fun upsertFile(
        githubAccessToken: String,
        content: MultipartFile,
        githubUsername: String,
        repo: String,
        path: String,
        commitMessage: String,
        sha: String = "",
    ): GithubContent {
        val request =
            GithubUpsertFileRequest(
                message = commitMessage,
                content = Base64Encoder.encodeBase64(content),
                sha = sha,
            )
        val response =
            githubResourceApiClient.upsertFile(
                bearerToken = BearerToken(githubAccessToken).format(),
                owner = githubUsername,
                repo = repo,
                path = path,
                request = request,
            )
        return response.content
    }

    fun createRepository(
        accessToken: String,
        request: GithubCreateRepositoryRequest,
    ) {
        githubResourceApiClient.createRepository(
            bearerToken = "Bearer $accessToken",
            request = request,
        )
    }

    fun isRepositoryExist(
        token: String,
        owner: String,
        repo: String,
    ): Boolean {
        return try {
            githubResourceApiClient.fetchRepository(
                token = token,
                owner = owner,
                repo = repo,
            )
            true
        } catch (e: FeignException.NotFound) {
            false
        }
    }

    private fun buildAuthRedirectionUrl(githubClientId: String): String {
        return "https://github.com/login/oauth/authorize?&scope=repo&client_id=$githubClientId"
    }

    private fun githubClientId(profile: Profile): String {
        return when (profile) {
            Profile.PROD -> githubProdClient.id
            Profile.DEV -> githubDevClient.id
        }
    }

    private fun githubClientSecret(profile: Profile): String {
        return when (profile) {
            Profile.PROD -> githubProdClient.secret
            Profile.DEV -> githubDevClient.secret
        }
    }

    companion object {
        private val log = logger {}
    }
}
