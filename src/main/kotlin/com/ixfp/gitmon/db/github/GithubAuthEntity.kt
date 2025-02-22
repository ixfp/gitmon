package com.ixfp.gitmon.db.github

import com.ixfp.gitmon.db.BaseEntity
import com.ixfp.gitmon.db.member.MemberEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "github_auth")
class GithubAuthEntity(
    @Id
    @Column(name = "member_id")
    var memberId: Long? = null,
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    val member: MemberEntity,
    @Column(name = "github_access_token", nullable = false)
    val githubAccessToken: String,
) : BaseEntity() {
    constructor(member: MemberEntity, githubAccessToken: String) : this(
        memberId = null,
        member = member,
        githubAccessToken = githubAccessToken,
    )
}
