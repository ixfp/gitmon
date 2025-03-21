package com.ixfp.gitmon.client.github

import com.ixfp.gitmon.client.github.request.GithubAccessTokenRequest
import com.ixfp.gitmon.client.github.request.GithubCreateRepositoryRequest
import com.ixfp.gitmon.client.github.request.GithubUpsertFileRequest
import com.ixfp.gitmon.client.github.response.GithubUserResponse
import com.ixfp.gitmon.common.util.Base64Encoder
import com.ixfp.gitmon.common.util.BearerToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

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

    fun upsertFile(
        githubAccessToken: String,
        content: MultipartFile,
        repo: String,
        path: String,
        commitMessage: String = "Upsert File by API",
    ): String {
        val request =
            GithubUpsertFileRequest(
                message = "Add New File",
                content = Base64Encoder.encodeBase64(content),
                sha = "",
            )
        val owner = "traceoflight" // TODO(KHJ): db에서 owner name 가져오는 부분 대응할 것
        val response =
            githubResourceApiClient.upsertFile(
                bearerToken = BearerToken(githubAccessToken).format(),
                owner = owner,
                repo = repo,
                path = path,
                request = request,
            )

        return response.sha
    }

    private fun createRepository(
        token: String,
        request: GithubCreateRepositoryRequest,
    ) {
        githubResourceApiClient.createRepository(token, request)
    }
}
