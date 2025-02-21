package com.ixfp.gitmon.db

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@MappedSuperclass
class BaseEntity(
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @UpdateTimestamp
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
