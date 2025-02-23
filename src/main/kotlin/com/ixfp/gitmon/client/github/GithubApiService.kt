package com.ixfp.gitmon.client.github

import com.ixfp.gitmon.client.github.request.GithubAccessTokenRequest
import com.ixfp.gitmon.client.github.request.GithubCreateRepositoryRequest
import com.ixfp.gitmon.client.github.response.GithubUserResponse
import com.ixfp.gitmon.common.util.BearerToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GithubApiService(
    private val githubOauth2ApiClient: GithubOauth2ApiClient,
    private val githubResourceApiClient: GithubResourceApiClient,
    @Value("\${oauth2.client.github.id}") private val githubClientId: String,
    @Value("\${oauth2.client.github.secret}") private val githubClientSecret: String,
) {
    fun getGithubUser(githubAccessToken: String): GithubUserResponse {
        return githubResourceApiClient.fetchUser(BearerToken(githubAccessToken).format())
    }

    fun getAccessTokenByCode(code: String): String {
        val request =
            GithubAccessTokenRequest(
                code = code,
                client_id = githubClientId,
                client_secret = githubClientSecret,
            )
        return githubOauth2ApiClient.fetchAccessToken(request).accessToken
    }

    // TODO(KHJ): dummy function, 관련 api 나오면 정리할 것
    fun hasRepository(githubAccessToken: String): Boolean {
        return false
    }

    private fun createRepository(
        token: String,
        request: GithubCreateRepositoryRequest,
    ) {
        githubResourceApiClient.createRepository(token, request)
    }
}
