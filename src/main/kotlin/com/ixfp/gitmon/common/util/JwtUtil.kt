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

    fun createAccessToken(memberExposedId: String): String {
        return Jwts.builder()
            .claims(
                mapOf(
                    "id" to memberExposedId,
                ),
            )
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_MILLISECONDS))
            .signWith(key)
            .compact()
    }

    fun createRefreshToken(memberExposedId: String): String {
        return Jwts.builder()
            .claims(
                mapOf(
                    "id" to memberExposedId,
                ),
            )
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_MILLISECONDS))
            .signWith(key)
            .compact()
    }

    fun parseAccessToken(token: String): AccessTokenPayload? {
        return try {
            val jws =
                Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
            val claims = jws.body
            val exposedId = claims["id"] as? String ?: return null
            AccessTokenPayload(exposedId)
        } catch (e: Exception) {
            return null
        }
    }

    companion object {
        private const val ACCESS_TOKEN_EXPIRE_MILLISECONDS = 1000 * 60 * 60 // 1시간
        private const val REFRESH_TOKEN_EXPIRE_MILLISECONDS = 1000 * 60 * 60 * 24 * 14 // 14일
    }
}

data class AccessTokenPayload(
    val exposedId: String,
)
