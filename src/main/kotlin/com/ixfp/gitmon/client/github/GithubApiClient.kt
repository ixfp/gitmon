package com.ixfp.gitmon.client.github

import com.ixfp.gitmon.common.util.BearerToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GithubApiClient(
    private val githubOauth2ApiClient: GithubOauth2ApiClient,
    private val githubResourceApiClient: GithubResourceApiClient,
    @Value("\${oauth2.client.github.id}") private val githubClientId: String,
    @Value("\${oauth2.client.github.secret}") private val githubClientSecret: String,
) {
    fun getUserByCode(code: String): GithubUserResponse {
        val accessToken = getAccessTokenByCode(code)
        return githubResourceApiClient.fetchUser(BearerToken(accessToken).format())
    }

    private fun getAccessTokenByCode(code: String): String {
        val request =
            GithubAccessTokenRequest(
                code = code,
                client_id = githubClientId,
                client_secret = githubClientSecret,
            )
        return githubOauth2ApiClient.fetchAccessToken(request).accessToken
    }
}
