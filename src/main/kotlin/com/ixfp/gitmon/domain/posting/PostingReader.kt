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
    fun findPostingListByMemberId(memberId: Long): List<Posting> {
        return postingRepository.findByRefMemberId(memberId).map { toPosting(it) }
    }

    fun findPostingById(postingId: Long): Posting {
        return postingRepository.findById(postingId)
            .orElseThrow { IllegalArgumentException("Posting with id $postingId not found") }
            .let { toPosting(it) }
    }

    private fun toPosting(entity: PostingEntity): Posting {
        return Posting(
            id = entity.id,
            title = entity.title,
            refMemberId = entity.refMemberId,
            githubFilePath = entity.githubFilePath,
            githubFileSha = entity.githubFileSha,
            githubDownloadUrl = entity.githubDownloadUrl,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }
}
