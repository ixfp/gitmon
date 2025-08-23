package com.ixfp.gitmon.domain.posting

data class PostingUpdateDto(
    val title: String,
    val githubFilePath: String,
    val githubFileSha: String,
    val githubDownloadUrl: String,
)
