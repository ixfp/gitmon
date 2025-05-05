package com.ixfp.gitmon.domain.member

import com.ixfp.gitmon.client.github.GithubApiService
import com.ixfp.gitmon.client.github.request.GithubCreateRepositoryRequest
import com.ixfp.gitmon.common.aop.WrapWith
import com.ixfp.gitmon.domain.member.exception.MemberExceptionStrategy
import com.ixfp.gitmon.domain.member.exception.MemberGithubAccessTokenNotFoundException
import org.springframework.stereotype.Service

@WrapWith(MemberExceptionStrategy::class)
@Service
class MemberService(
    private val githubApiService: GithubApiService,
    private val memberWriter: MemberWriter,
    private val memberReader: MemberReader,
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
        githubApiService.createRepository(githubAccessToken, githubRequest)
        memberWriter.upsertRepo(member, repoName)
    }

    fun isRepoNameAvailable(
        member: Member,
        repoName: String,
    ): Boolean {
        if (isRepoNameDuplicate(member, repoName)) {
            return false
        }
        return true
    }

    fun findGithubRepoUrl(githubUsername: String): String? {
        val member = memberReader.findByGithubUsername(githubUsername) ?: return null

        if (member.repoName == null) {
            return null
        }

        return buildGithubRepoUrl(member.githubUsername, member.repoName)
    }

    private fun buildGithubRepoUrl(
        githubUsername: String,
        repoName: String,
    ): String {
        return "https://github.com/$githubUsername/$repoName"
    }

    private fun isRepoNameDuplicate(
        member: Member,
        repoName: String,
    ): Boolean {
        val githubAccessToken =
            memberReader.findAccessTokenByMemberId(member.id)
                ?: throw MemberGithubAccessTokenNotFoundException()
        return githubApiService.isRepositoryExist(
            token = "token $githubAccessToken",
            owner = member.githubUsername,
            repo = repoName,
        )
    }
}
