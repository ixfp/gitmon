package com.ixfp.gitmon.controller

import com.ixfp.gitmon.common.util.JwtUtil
import com.ixfp.gitmon.controller.request.CreatePostingRequest
import com.ixfp.gitmon.domain.auth.AuthService
import com.ixfp.gitmon.domain.posting.PostingService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.naming.AuthenticationException

@RequestMapping("/api/v1/posting")
@RestController
class PostingController(
    private val jwtUtil: JwtUtil,
    private val authService: AuthService,
    private val postingService: PostingService,
) {
    @PostMapping
    fun createPosting(
        @RequestBody request: CreatePostingRequest,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<HttpStatus> {
        try {
            val accessToken = authorizationHeader.removePrefix("Bearer ")
            val memberExposedId = jwtUtil.parseAccessToken(accessToken)?.exposedId
            if (memberExposedId == null) {
                throw AuthenticationException("유효하지 않은 사용자 토큰")
            }
            val member = authService.getMemberByExposedId(memberExposedId)
            if (member == null) {
                throw Error("토큰에 해당하는 사용자를 찾을 수 없음")
            }

            postingService.create(member, request.title, request.content)

            return ResponseEntity(HttpStatus.CREATED)
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}