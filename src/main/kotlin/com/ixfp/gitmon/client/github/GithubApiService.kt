package com.ixfp.gitmon.client.github

import com.fasterxml.jackson.core.JsonProcessingException
import com.ixfp.gitmon.client.github.exception.GithubApiException
import com.ixfp.gitmon.client.github.exception.GithubInvalidAuthCodeException
import com.ixfp.gitmon.client.github.exception.GithubNetworkException
import com.ixfp.gitmon.client.github.exception.GithubResponseParsingException
import com.ixfp.gitmon.client.github.exception.GithubUnexpectedException
import com.ixfp.gitmon.client.github.request.GithubAccessTokenRequest
import com.ixfp.gitmon.client.github.request.GithubUpsertFileRequest
import com.ixfp.gitmon.client.github.response.GithubContent
import com.ixfp.gitmon.client.github.response.GithubUserResponse
import com.ixfp.gitmon.common.util.Base64Encoder
import com.ixfp.gitmon.common.util.BearerToken
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Component
class GithubApiService(
    private val githubOauth2ApiClient: GithubOauth2ApiClient,
    private val githubResourceApiClient: GithubResourceApiClient,
    @Value("\${oauth2.client.github.id}") private val githubClientId: String,
    @Value("\${oauth2.client.github.secret}") private val githubClientSecret: String,
) {
    fun getGithubUser(githubAccessToken: String): GithubUserResponse {
        return runCatchingGithub {
            githubResourceApiClient.fetchUser(BearerToken(githubAccessToken).format())
        }
    }

    fun getAccessTokenByCode(code: String): String {
        val request = GithubAccessTokenRequest(code, githubClientId, githubClientSecret)
        return runCatchingGithub {
            val response = githubOauth2ApiClient.fetchAccessToken(request)
            response.accessToken ?: throw GithubInvalidAuthCodeException("response=$response")
        }
    }

    fun upsertFile(
        githubAccessToken: String,
        content: MultipartFile,
        githubUsername: String,
        repo: String,
        path: String,
        commitMessage: String = "Upsert File by API",
    ): GithubContent {
        val request =
            GithubUpsertFileRequest(
                message = "Add New File",
                content = Base64Encoder.encodeBase64(content),
                sha = "",
            )
        return runCatchingGithub {
            val response =
                githubResourceApiClient.upsertFile(
                    bearerToken = BearerToken(githubAccessToken).format(),
                    owner = githubUsername,
                    repo = repo,
                    path = path,
                    request = request,
                )
            response.content
        }
    }

    private inline fun <T> runCatchingGithub(block: () -> T): T {
        return try {
            block()
        } catch (ex: GithubApiException) {
            throw ex
        } catch (ex: IOException) {
            throw GithubNetworkException(cause = ex)
        } catch (ex: JsonProcessingException) {
            throw GithubResponseParsingException(cause = ex)
        } catch (ex: Exception) {
            throw GithubUnexpectedException(cause = ex)
        }
    }

    companion object {
        private val log = logger {}
    }
}

