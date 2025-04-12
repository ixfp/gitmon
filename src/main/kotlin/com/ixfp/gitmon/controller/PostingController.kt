package com.ixfp.gitmon.controller

import com.ixfp.gitmon.common.type.ApiErrorType
import com.ixfp.gitmon.common.type.ApiResponse
import com.ixfp.gitmon.config.web.AUTHENTICATED_MEMBER
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.posting.PostingService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
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
    ): ApiResponse<Unit> {
        try {
            postingService.create(member, title, content)
            return ApiResponse.success()
        } catch (e: Exception) {
            val errorType =
                when (e) {
                    is IllegalArgumentException -> ApiErrorType.BAD_REQUEST
                    else -> ApiErrorType.INTERNAL_SERVER_ERROR
                }
            log.error { "포스팅 생성 중 에러 발생: ${e.message}" }
            return ApiResponse.error(errorType)
        }
    }

    companion object {
        private val log = logger {}
    }
}
