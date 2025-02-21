package com.ixfp.gitmon.db.github

import com.ixfp.gitmon.db.BaseEntity
import com.ixfp.gitmon.db.member.MemberEntity
import jakarta.persistence.*

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
        githubAccessToken = githubAccessToken
    )
}
