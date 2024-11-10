package com.ixfp.gitmon.common.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtil(
    @Value("\${jwt.secret}") key: String,
) {
    private val key = Keys.hmacShaKeyFor(key.toByteArray())

    fun createAccessToken(username: String): String {
        return Jwts.builder()
            .claims(
                mapOf(
                    "username" to username,
                ),
            )
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + TOKEN_EXPIRE_MILLISECONDS))
            .signWith(key)
            .compact()
    }

    companion object {
        private const val TOKEN_EXPIRE_MILLISECONDS = 1000 * 60 * 60 * 10 // 10시간
    }
}
