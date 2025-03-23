package com.ixfp.gitmon.domain.posting

import com.ixfp.gitmon.client.github.GithubApiService
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.member.MemberReader
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
        val githubAccessToken =
            memberReader.findAccessTokenByMemberId(member.id)
                ?: throw IllegalArgumentException("Github 엑세스 토큰이 없습니다.")

        if (member.repoName == null) {
            throw IllegalArgumentException("포스팅 레포지토리가 없습니다.")
        }

        githubApiService.upsertFile(
            githubAccessToken = githubAccessToken,
            content = content,
            githubUsername = member.githubUsername,
            repo = member.repoName,
            path = "$title.md",
        )
    }
}
