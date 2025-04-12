package com.ixfp.gitmon.controller

import com.ixfp.gitmon.config.docs.ACCESS_TOKEN
import com.ixfp.gitmon.domain.member.Member
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Posting", description = "게시글(포스팅) 관련 API")
interface IPostingController {
    @Operation(
        summary = "포스팅 생성",
        description = "포스팅을 생성합니다.",
        security = [SecurityRequirement(name = ACCESS_TOKEN)],
    )
    fun createPosting(
        title: String,
        content: MultipartFile,
        member: Member,
    ): ResponseEntity<HttpStatus>
}
