package com.ixfp.gitmon.db.member

import com.ixfp.gitmon.db.BaseEntity
import com.ixfp.gitmon.domain.member.Member
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "member")
class MemberEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    var memberId: Long = 0,
    @Column(name = "exposed_id")
    val exposedId: String,
    @Column(name = "github_id")
    val githubId: Long,
    @Column(name = "github_username")
    val githubUsername: String,
    @Column(name = "repo_name")
    var repoName: String? = null,
) : BaseEntity() {
    companion object {
        fun toMember(entity: MemberEntity): Member {
            with(entity) {
                return Member(
                    id = memberId,
                    exposedId = exposedId,
                    githubId = githubId,
                    githubUsername = githubUsername,
                    repoName = repoName,
                )
            }
        }
    }
}
