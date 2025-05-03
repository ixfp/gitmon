package com.ixfp.gitmon.domain.posting

import com.ixfp.gitmon.db.posting.PostingEntity
import com.ixfp.gitmon.db.posting.PostingRepository
import org.springframework.stereotype.Component

@Component
class PostingWriter(
    private val postingRepository: PostingRepository,
) {
    fun write(postingWriteDto: PostingWriteDto) {
        val entity =
            PostingEntity(
                title = postingWriteDto.title,
                refMemberId = postingWriteDto.member.id,
                githubFilePath = postingWriteDto.githubFilePath,
                githubFileSha = postingWriteDto.githubFileSha,
                githubDownloadUrl = postingWriteDto.githubDownloadUrl,
            )
        postingRepository.save(entity)
    }
}
