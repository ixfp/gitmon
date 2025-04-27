package com.ixfp.gitmon.domain.posting

import com.ixfp.gitmon.db.posting.PostingEntity
import com.ixfp.gitmon.db.posting.PostingRepository
import org.springframework.stereotype.Component

@Component
class PostingWriter(
    private val postingRepository: PostingRepository,
) {
    fun write(posting: Posting) {
        val entity =
            PostingEntity(
                title = posting.title,
                refMemberId = posting.member.id,
                githubFilePath = posting.githubFilePath,
                githubFileSha = posting.githubFileSha,
                githubDownloadUrl = posting.githubDownloadUrl,
            )
        postingRepository.save(entity)
    }
}
