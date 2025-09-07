package com.ixfp.gitmon.controller

import com.ixfp.gitmon.config.web.AUTHENTICATED_MEMBER
import com.ixfp.gitmon.controller.type.ApiResponseBody
import com.ixfp.gitmon.controller.util.ApiResponseHelper
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.posting.PostingReadDto
import com.ixfp.gitmon.domain.posting.PostingService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/posting")
class PostingController(
    private val postingService: PostingService,
) : IPostingController {
    @PostMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
    )
    override fun createPosting(
        @RequestParam title: String,
        @RequestPart content: MultipartFile,
        @RequestAttribute(AUTHENTICATED_MEMBER) member: Member,
    ): ResponseEntity<ApiResponseBody<PostingReadDto>> {
        val savedPostingId = postingService.create(member, title, content)
        val posting = postingService.findPosting(savedPostingId)
        return ApiResponseHelper.created(posting)
    }

    @PutMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
    )
    override fun updatePosting(
        @RequestParam id: Long,
        @RequestParam title: String,
        @RequestPart content: MultipartFile,
        @RequestAttribute(AUTHENTICATED_MEMBER) member: Member,
    ): ResponseEntity<ApiResponseBody<PostingReadDto>> {
        val updatedPosting = postingService.update(member, id, title, content)
        val posting = postingService.findPosting(updatedPosting)
        return ApiResponseHelper.success(posting)
    }

    @GetMapping("/{exposedMemberId}")
    override fun getPostingList(
        @PathVariable exposedMemberId: String,
    ): ResponseEntity<ApiResponseBody<List<PostingReadDto>>> {
        val postingList = postingService.findPostingListByMemberExposedId(exposedMemberId)
        return ApiResponseHelper.success(postingList)
    }

    @GetMapping("/github/{githubUsername}")
    override fun getPostingListByGithubUsername(
        @PathVariable githubUsername: String,
    ): ResponseEntity<ApiResponseBody<List<PostingReadDto>>> {
        val postingList = postingService.findPostingListByGithubUsername(githubUsername)
        return ApiResponseHelper.success(postingList)
    }

    @GetMapping("/github/{githubUsername}/{postingId}")
    override fun getPosting(
        @PathVariable githubUsername: String,
        @PathVariable postingId: Long,
    ): ResponseEntity<ApiResponseBody<PostingReadDto>> {
        val posting = postingService.findPosting(githubUsername, postingId)
        return ApiResponseHelper.success(posting)
    }

    @PostMapping(
        "/images",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
    )
    override fun uploadImage(
        @RequestPart image: MultipartFile,
        @RequestAttribute(AUTHENTICATED_MEMBER) member: Member,
    ): ResponseEntity<ApiResponseBody<String>> {
        val imageUrl = postingService.uploadImage(member, image)
        return ApiResponseHelper.created(imageUrl)
    }

    companion object {
        private val log = logger {}
    }
}
