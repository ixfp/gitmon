package com.ixfp.gitmon.domain.posting

import com.ixfp.gitmon.client.github.GithubApiService
import com.ixfp.gitmon.common.aop.WrapWith
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.member.MemberReader
import com.ixfp.gitmon.domain.posting.exception.PostingExceptionStrategy
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@WrapWith(PostingExceptionStrategy::class)
@Service
class PostingService(
    private val memberReader: MemberReader,
    private val githubApiService: GithubApiService,
    private val postingReader: PostingReader,
    private val postingWriter: PostingWriter,
) {
    fun findPostingListByMemberExposedId(memberExposedId: String): List<PostingReadDto> {
        val member =
            memberReader.findByExposedId(memberExposedId)
                ?: throw IllegalArgumentException("존재하지 않는 회원입니다.")
        return postingReader.findPostingListByMemberId(member.id)
    }

    fun create(
        member: Member,
        title: String,
        content: MultipartFile,
    ) {
        log.info { "PostingService#create start. member=$member, title=$title, content.size=${content.size}" }
        val githubAccessToken =
            memberReader.findAccessTokenByMemberId(member.id)
                ?: throw IllegalArgumentException("Github 엑세스 토큰이 없습니다.")

        if (member.repoName == null) {
            throw IllegalArgumentException("포스팅 레포지토리가 없습니다.")
        }

        val githubContent =
            githubApiService.upsertFile(
                githubAccessToken = githubAccessToken,
                content = content,
                githubUsername = member.githubUsername,
                repo = member.repoName,
                path = "$title.md",
            )

        postingWriter.write(
            PostingWriteDto(
                title = title,
                member = member,
                githubFilePath = githubContent.path,
                githubFileSha = githubContent.sha,
                githubDownloadUrl = githubContent.download_url,
            ),
        )

        log.info { "PostingService#create end. contentSha=${githubContent.sha}" }
    }

    private fun uploadImage(
        member: Member,
        content: MultipartFile,
    ): String {
        val githubAccessToken =
            memberReader.findAccessTokenByMemberId(member.id)
                ?: throw IllegalArgumentException("Github 엑세스 토큰이 없습니다.")

        if (member.repoName == null) {
            throw IllegalArgumentException("포스팅 레포지토리가 없습니다.")
        }

        val extension = resolveExtension(content) ?: throw IllegalArgumentException("파일의 확장자가 이미지가 아닙니다.")
        val imageId = UUID.randomUUID().toString()
        val filename = "$imageId.$extension"
        val path = "images/$filename"

        val githubContent =
            githubApiService.upsertFile(
                githubAccessToken = githubAccessToken,
                content = content,
                githubUsername = member.githubUsername,
                repo = member.repoName,
                path = path,
            )

        return githubContent.download_url
    }

    private fun resolveExtension(file: MultipartFile): String? {
        val contentType = file.contentType
        if (contentType != null && contentType.startsWith("image/")) {
            val subtype = contentType.substringAfter("image/")
            if (subtype.isNotBlank()) {
                return subtype
            }
        }
        val extension = file.originalFilename?.substringAfterLast('.', "")
        if (!extension.isNullOrBlank()) {
            return extension
        }
        return null
    }

    companion object {
        private val log = logger {}
    }
}
