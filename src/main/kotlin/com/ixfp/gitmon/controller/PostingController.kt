package com.ixfp.gitmon.controller

import com.ixfp.gitmon.config.docs.ACCESS_TOKEN
import com.ixfp.gitmon.config.web.AUTHENTICATED_MEMBER
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.posting.PostingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RequestMapping("/api/v1/posting")
@RestController
class PostingController(
    private val postingService: PostingService,
) {
    @Operation(
        summary = "포스팅 생성",
        description = "포스팅을 생성합니다.",
        security = [SecurityRequirement(name = ACCESS_TOKEN)],
    )
    @PostMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
    )
    fun createPosting(
        @RequestParam title: String,
        @RequestPart content: MultipartFile,
        @RequestAttribute(AUTHENTICATED_MEMBER) member: Member,
    ): ResponseEntity<HttpStatus> {
        try {
            postingService.create(member, title, content)
            return ResponseEntity(HttpStatus.CREATED)
        } catch (e: Exception) {
            val status =
                when (e) {
                    is IllegalArgumentException -> HttpStatus.BAD_REQUEST
                    else -> HttpStatus.INTERNAL_SERVER_ERROR
                }
            return ResponseEntity(status)
        }
    }
}
