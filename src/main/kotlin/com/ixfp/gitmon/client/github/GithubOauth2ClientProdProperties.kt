package com.ixfp.gitmon.client.github

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth2.client.github")
data class GithubOauth2ClientProdProperties(
    val id: String,
    val secret: String,
)
