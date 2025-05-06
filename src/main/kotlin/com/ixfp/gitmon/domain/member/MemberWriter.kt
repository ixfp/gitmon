package com.ixfp.gitmon.domain.member

import com.ixfp.gitmon.common.aop.WrapWith
import com.ixfp.gitmon.db.exception.DbExceptionStrategy
import com.ixfp.gitmon.db.github.GithubAuthEntity
import com.ixfp.gitmon.db.github.GithubAuthRepository
import com.ixfp.gitmon.db.member.MemberEntity
import com.ixfp.gitmon.db.member.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@WrapWith(DbExceptionStrategy::class)
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

    @Transactional
    fun upsertGithubAccessToken(
        member: Member,
        githubAccessToken: String,
    ) {
        val memberEntity =
            memberRepository.findById(member.id)
                .orElseThrow { Error("회원을 찾을 수 없음") }
        val existingGithubAuth = githubAuthRepository.findByMemberId(memberEntity.memberId)

        if (existingGithubAuth != null) {
            existingGithubAuth.githubAccessToken = githubAccessToken
            githubAuthRepository.save(existingGithubAuth)
        } else {
            val githubAuthEntity = GithubAuthEntity(memberEntity, githubAccessToken)
            githubAuthRepository.save(githubAuthEntity)
        }
    }

    @Transactional
    fun upsertRepo(
        member: Member,
        repoName: String,
    ) {
        memberRepository.findById(member.id)
            .orElseThrow { Error("회원을 찾을 수 없음") }
            .apply { this.repoName = repoName }
    }
}
