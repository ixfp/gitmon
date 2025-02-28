package com.ixfp.gitmon.db.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface MemberRepository : JpaRepository<MemberEntity, Long> {
    override fun <S : MemberEntity> save(entity: S): S

    override fun findById(memberId: Long): Optional<MemberEntity>

    fun findByGithubId(githubId: Long): MemberEntity?

    fun findByGithubUsername(githubUsername: String): List<MemberEntity>

    fun findByExposedId(exposedId: String): MemberEntity?

    @Modifying
    @Query("UPDATE MemberEntity m SET m.repoName = :repoName WHERE m.memberId = :memberId")
    fun updateRepoNameByMemberId(
        memberId: Long,
        repoName: String,
    ): Int
}
