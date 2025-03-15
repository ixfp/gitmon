package com.ixfp.gitmon.domain.posting

import com.ixfp.gitmon.domain.member.Member
import com.ixfp.gitmon.domain.member.MemberReader
import org.springframework.stereotype.Service

@Service
class PostingService(
    private val memberReader: MemberReader,
) {
    fun create(
        member: Member,
        title: String,
        content: String,
    ) {
        val githubAccessToken =
            memberReader.findAccessTokenByMemberId(member.id)
                ?: throw Error("Github 엑세스 토큰 없음")

        // Todo: githubService 연동하여 posting upsert API 호출
    }
}
