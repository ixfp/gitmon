package com.ixfp.gitmon.domain.member

import com.ixfp.gitmon.db.member.MemberEntity
import com.ixfp.gitmon.db.member.MemberRepository
import org.springframework.stereotype.Component

@Component
class MemberReader(
    private val memberRepository: MemberRepository,
) {
    fun findById(id: Long): Member? {
        return memberRepository.findById(id).orElse(null)?.let { toMember(it) }
    }

    fun findByGithubId(githubId: Long): Member? {
        return memberRepository.findByGithubId(githubId)?.let { toMember(it) }
    }

    private fun toMember(entity: MemberEntity): Member {
        with(entity) {
            return Member(
                id = id,
                exposedId = exposedId,
                githubId = githubId,
                githubUsername = githubUsername,
                repoName = repoName,
            )
        }
    }
}
