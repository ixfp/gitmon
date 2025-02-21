package com.ixfp.gitmon.db.github

import org.springframework.data.jpa.repository.JpaRepository


interface GithubAuthRepository : JpaRepository<GithubAuthEntity, Long> {
    override fun <S : GithubAuthEntity> save(entity: S): S
}