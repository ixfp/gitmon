package com.ixfp.gitmon.controller

import com.ixfp.gitmon.common.util.JwtUtil
import com.ixfp.gitmon.controller.request.CreateRepoRequest
import com.ixfp.gitmon.domain.auth.AuthService
import com.ixfp.gitmon.domain.member.MemberService
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.naming.AuthenticationException

@RequestMapping("/api/v1/member")
@RestController()
class MemberController(
    private val jwtUtil: JwtUtil,
    private val authService: AuthService,
    private val memberService: MemberService,
) {
    // 이미 설정된 레포지토리가 있다면 갱신하고, 없다면 생성한다.
    @PutMapping("/repo")
    fun upsertRepo(
        @RequestBody request: CreateRepoRequest,
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<HttpStatus> {
        try {
            // TODO: 엑세스토큰으로 id 혹은 member 객체로 변환하는 필터 만들기
            val accessToken = authorizationHeader.removePrefix("Bearer ")
            val memberExposedId = jwtUtil.parseAccessToken(accessToken)?.exposedId
            if (memberExposedId === null) {
                throw AuthenticationException("유효하지 않은 사용자 토큰")
            }
            val member = authService.getMemberByExposedId(memberExposedId)
            if (member === null) {
                throw Error("토큰에 해당하는 사용자를 찾을 수 없음")
            }
            val githubAccessToken = authService.getGithubAccessToken(member.memberId)
            if (githubAccessToken === null) {
                throw Error("사용자의 깃허브 토큰을 찾을 수 없음")
            }
            memberService.upsertRepo(member, request.name, githubAccessToken)
            return ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (e: Exception) {
            log.error(e) { "Failed to create repository: ${e.message}" }
            val status =
                when (e) {
                    is IllegalArgumentException -> HttpStatus.BAD_REQUEST
                    is AuthenticationException -> HttpStatus.UNAUTHORIZED
                    else -> HttpStatus.INTERNAL_SERVER_ERROR
                }
            return ResponseEntity.status(status).build()
        }
    }

    companion object {
        private val log = logger {}
    }
}
