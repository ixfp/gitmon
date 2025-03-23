package com.ixfp.gitmon.domain.posting

import com.ixfp.gitmon.client.github.GithubApiService
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.member.MemberReader
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class PostingService(
    private val memberReader: MemberReader,
    private val githubApiService: GithubApiService,
) {
    fun create(
        member: Member,
        title: String,
        content: MultipartFile,
    ) {
        log.info { "PostingService#create start. member=$member, title=$title, content.size=${content.size}" }
        val githubAccessToken =
            memberReader.findAccessTokenByMemberId(member.id)
                ?: throw IllegalArgumentException("Github 엑세스 토큰이 없습니다.")

        if (member.repoName == null) {
            throw IllegalArgumentException("포스팅 레포지토리가 없습니다.")
        }

        val contentSha =
            githubApiService.upsertFile(
                githubAccessToken = githubAccessToken,
                content = content,
                githubUsername = member.githubUsername,
                repo = member.repoName,
                path = "$title.md",
            )
        log.info { "PostingService#create end. contentSha=$contentSha" }
    }

    companion object {
        private val log = logger {}
    }
}
