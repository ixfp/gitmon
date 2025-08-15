package com.ixfp.gitmon.domain.posting

import com.ixfp.gitmon.common.aop.WrapWith
import com.ixfp.gitmon.db.exception.DbExceptionStrategy
import com.ixfp.gitmon.db.posting.PostingEntity
import com.ixfp.gitmon.db.posting.PostingRepository
import org.springframework.stereotype.Component

@WrapWith(DbExceptionStrategy::class)
@Component
class PostingWriter(
    private val postingRepository: PostingRepository,
) {
    fun write(postingWriteDto: PostingWriteDto): Long {
        val entity =
            PostingEntity(
                title = postingWriteDto.title,
                refMemberId = postingWriteDto.member.id,
                githubFilePath = postingWriteDto.githubFilePath,
                githubFileSha = postingWriteDto.githubFileSha,
                githubDownloadUrl = postingWriteDto.githubDownloadUrl,
            )
        val saved = postingRepository.save(entity)

        return saved.id
    }
}
