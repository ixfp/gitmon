package com.ixfp.gitmon.domain.member

import com.ixfp.gitmon.client.github.GithubResourceApiClient
import com.ixfp.gitmon.client.github.request.GithubCreateRepositoryRequest
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val githubResourceApiClient: GithubResourceApiClient,
    private val memberWriter: MemberWriter,
) {
    fun upsertRepo(
        member: Member,
        repoName: String,
        githubAccessToken: String,
    ) {
        val githubRequest =
            GithubCreateRepositoryRequest(
                name = repoName,
                description = "Powered By Gitmon",
                homepage = "https://gitmon.blog",
                private = false,
            )
        // TODO: DB 레포지토리 업데이트 오류 시 생성된 레포지토리를 지워야 함
        githubResourceApiClient.createRepository("Bearer $githubAccessToken", githubRequest)
        memberWriter.upsertRepo(member, repoName)
    }
}
