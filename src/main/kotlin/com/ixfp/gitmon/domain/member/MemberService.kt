package com.ixfp.gitmon.domain.member

import com.ixfp.gitmon.client.github.GithubResourceApiClient
import com.ixfp.gitmon.client.github.request.GithubCreateRepositoryRequest
import com.ixfp.gitmon.db.member.MemberEntity
import com.ixfp.gitmon.db.member.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val githubResourceApiClient: GithubResourceApiClient,
    private val memberRepository: MemberRepository,
) {
    @Transactional()
    fun upsertRepo(
        member: MemberEntity,
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
        memberRepository.updateRepoNameByMemberId(member.memberId, repoName)
    }
}
