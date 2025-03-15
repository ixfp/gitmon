package com.ixfp.gitmon.controller

import com.ixfp.gitmon.common.const.AUTHENTICATED_MEMBER
import com.ixfp.gitmon.controller.request.CreatePostingRequest
import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.posting.PostingService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/posting")
@RestController
class PostingController(
    private val postingService: PostingService,
) {
    @PostMapping
    fun createPosting(
        @RequestBody request: CreatePostingRequest,
        @RequestAttribute(AUTHENTICATED_MEMBER) member: Member,
    ): ResponseEntity<HttpStatus> {
        try {
            postingService.create(member, request.title, request.content)
            return ResponseEntity(HttpStatus.CREATED)
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
