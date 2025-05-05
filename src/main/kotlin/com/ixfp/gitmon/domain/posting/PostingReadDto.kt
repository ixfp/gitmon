package com.ixfp.gitmon.domain.posting

import java.time.LocalDateTime

data class PostingReadDto(
    val id: Long,
    val title: String,
    val githubDownloadUrl: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
