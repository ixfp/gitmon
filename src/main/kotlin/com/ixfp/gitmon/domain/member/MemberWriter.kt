package com.ixfp.gitmon.domain.member

import com.ixfp.gitmon.db.github.GithubAuthEntity
import com.ixfp.gitmon.db.github.GithubAuthRepository
import com.ixfp.gitmon.db.member.MemberEntity
import com.ixfp.gitmon.db.member.MemberRepository
import org.springframework.stereotype.Component

@Component
class MemberWriter(
    private val memberRepository: MemberRepository,
    private val githubAuthRepository: GithubAuthRepository,
) {
    fun save(
        exposedId: String,
        githubId: Long,
        githubUserName: String,
    ): Member {
        val memberEntity =
            MemberEntity(
                exposedId = exposedId,
                githubId = githubId,
                githubUsername = githubUserName,
            )
        val savedMemberEntity = memberRepository.save(memberEntity)
        return MemberEntity.toMember(savedMemberEntity)
    }

    fun upsertGithubAccessToken(
        member: Member,
        githubAccessToken: String,
    ) {
        val memberEntity =
            MemberEntity(
                memberId = member.id,
                exposedId = member.exposedId,
                githubId = member.githubId,
                githubUsername = member.githubUsername,
                repoName = member.repoName,
            )
        val githubAuthEntity = GithubAuthEntity(memberEntity, githubAccessToken)
        githubAuthRepository.save(githubAuthEntity)
    }

    fun upsertRepo(
        member: Member,
        repoName: String,
    ) {
        memberRepository.updateRepoNameByMemberId(member.id, repoName)
    }
}
