package com.ixfp.gitmon.db.member

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface MemberRepository : JpaRepository<MemberEntity, Long> {
    override fun <S : MemberEntity> save(entity: S): S

    override fun findById(memberId: Long): Optional<MemberEntity>

    fun findByGithubId(githubId: Long): MemberEntity?

    fun findByGithubUsername(githubUsername: String): MemberEntity?

    fun findByExposedId(exposedId: String): MemberEntity?
}
