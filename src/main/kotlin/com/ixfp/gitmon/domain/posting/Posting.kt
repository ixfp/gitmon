package com.ixfp.gitmon.domain.posting

import com.ixfp.gitmon.domain.member.Member

data class Posting(
    val title: String,
    val member: Member,
    val githubFilePath: String,
    val githubFileSha: String,
    val githubDownloadUrl: String,
)
