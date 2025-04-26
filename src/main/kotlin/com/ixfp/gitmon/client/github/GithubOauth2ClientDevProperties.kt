package com.ixfp.gitmon.client.github

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth2.client-dev.github")
data class GithubOauth2ClientDevProperties(
    val id: String,
    val secret: String,
)
