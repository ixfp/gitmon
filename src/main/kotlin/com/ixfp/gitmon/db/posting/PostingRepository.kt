package com.ixfp.gitmon.db.posting

import org.springframework.data.jpa.repository.JpaRepository

interface PostingRepository : JpaRepository<PostingEntity, Long>
