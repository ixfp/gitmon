package com.ixfp.gitmon.db.member

import org.springframework.data.jpa.repository.JpaRepository

import java.util.Optional

interface MemberRepository : JpaRepository<MemberEntity, Long> {
    override fun findById(memberId: Long): Optional<MemberEntity>

    fun findByGithubId(githubId: Int): MemberEntity?

    fun findByGithubUsername(githubUsername: String): List<MemberEntity>
}
