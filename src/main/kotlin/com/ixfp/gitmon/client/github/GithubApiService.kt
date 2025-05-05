package com.ixfp.gitmon.client.github

import com.fasterxml.jackson.core.JsonProcessingException
import com.ixfp.gitmon.client.github.exception.GithubApiException
import com.ixfp.gitmon.client.github.exception.GithubNetworkException
import com.ixfp.gitmon.client.github.exception.GithubResponseParsingException
import com.ixfp.gitmon.client.github.exception.GithubUnexpectedException
import com.ixfp.gitmon.client.github.request.GithubAccessTokenRequest
import com.ixfp.gitmon.client.github.request.GithubUpsertFileRequest
import com.ixfp.gitmon.client.github.response.GithubContent
import com.ixfp.gitmon.client.github.response.GithubUserResponse
import com.ixfp.gitmon.common.type.Profile
import com.ixfp.gitmon.common.util.Base64Encoder
import com.ixfp.gitmon.common.util.BearerToken
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Component
class GithubApiService(
    private val githubOauth2ApiClient: GithubOauth2ApiClient,
    private val githubResourceApiClient: GithubResourceApiClient,
    private val githubProdClient: GithubOauth2ClientProdProperties,
    private val githubDevClient: GithubOauth2ClientDevProperties,
) {
    fun getGithubUser(githubAccessToken: String): GithubUserResponse {
        return runCatchingGithub {
            githubResourceApiClient.fetchUser(BearerToken(githubAccessToken).format())
        }
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
        log.info { "GithubApiService#getAccessTokenByCode start. code=$code, profile=$profile" }
        val request =
            GithubAccessTokenRequest(
                code = code,
                client_id = githubClientId(profile),
                client_secret = githubClientSecret(profile),
            )

        runCatchingGithub {
            val response = githubOauth2ApiClient.fetchAccessToken(request)
            log.info { "GithubApiService#getAccessTokenByCode end. response=$response" }
            if (response.accessToken == null) {
                throw RuntimeException("Failed to get access token from github. response=$response")
            }
            return response.accessToken
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
