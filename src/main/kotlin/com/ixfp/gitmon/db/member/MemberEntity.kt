package com.ixfp.gitmon.db.member

import com.ixfp.gitmon.db.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "member")
class MemberEntity(
    @Column(name = "member_id")
    override val id: Long,
    val exposedId: String,
    val githubId: Int,
    val githubUsername: String,
    val repoName: String,
) : BaseEntity(id)
