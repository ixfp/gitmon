package com.ixfp.gitmon.domain.posting

import com.ixfp.gitmon.db.posting.PostingEntity
import com.ixfp.gitmon.db.posting.PostingRepository
import org.springframework.stereotype.Component

@Component
class PostingReader(
    private val postingRepository: PostingRepository,
) {
    fun findPostingListByMemberId(memberId: Long): List<PostingReadDto> {
        return postingRepository.findByRefMemberId(memberId).map { toPostingItem(it) }
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
