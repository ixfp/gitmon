package com.ixfp.gitmon.domain.posting

import java.time.LocalDateTime

data class Posting(
    val id: Long,
    val title: String,
    val refMemberId: Long,
    val githubFilePath: String,
    val githubFileSha: String,
    val githubDownloadUrl: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
