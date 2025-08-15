package com.ixfp.gitmon.domain.posting

import com.ixfp.gitmon.common.aop.WrapWith
import com.ixfp.gitmon.db.exception.DbExceptionStrategy
import com.ixfp.gitmon.db.posting.PostingEntity
import com.ixfp.gitmon.db.posting.PostingRepository
import org.springframework.stereotype.Component

@WrapWith(DbExceptionStrategy::class)
@Component
class PostingReader(
    private val postingRepository: PostingRepository,
) {
    fun findPostingListByMemberId(memberId: Long): List<PostingReadDto> {
        return postingRepository.findByRefMemberId(memberId).map { toPostingItem(it) }
    }

    fun findPostingById(postingId: Long): PostingReadDto {
        return postingRepository.findById(postingId)
            .orElseThrow { IllegalArgumentException("Posting with id $postingId not found") }
            .let { toPostingItem(it) }
    }

    private fun toPostingItem(entity: PostingEntity): PostingReadDto {
        return PostingReadDto(
            id = entity.id,
            title = entity.title,
            githubDownloadUrl = entity.githubDownloadUrl,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }
}
