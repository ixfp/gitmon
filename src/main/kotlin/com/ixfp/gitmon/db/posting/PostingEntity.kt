package com.ixfp.gitmon.db.posting

import com.ixfp.gitmon.db.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "posting")
class PostingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    val refMemberId: Long,
    var githubFilePath: String,
    var githubFileSha: String,
    var githubDownloadUrl: String,
) : BaseEntity()
