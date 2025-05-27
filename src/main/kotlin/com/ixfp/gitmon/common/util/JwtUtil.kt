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
        return buildToken(memberExposedId, ACCESS_TOKEN_EXPIRE_MILLISECONDS)
    }

    fun createRefreshToken(memberExposedId: String): String {
        return buildToken(memberExposedId, REFRESH_TOKEN_EXPIRE_MILLISECONDS)
    }

    private fun buildToken(
        memberExposedId: String,
        tokenValidityMillis: Int,
    ): String {
        return Jwts.builder()
            .claims(
                mapOf(
                    "id" to memberExposedId,
                ),
            )
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + tokenValidityMillis))
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
        private const val ONE_SECOND_MILLIS = 1000
        private const val ONE_MINUTE_MILLIS = 60 * ONE_SECOND_MILLIS
        private const val ONE_HOUR_MILLIS = 60 * ONE_MINUTE_MILLIS
        private const val ONE_DAY_MILLIS = 24 * ONE_HOUR_MILLIS
        private const val ACCESS_TOKEN_EXPIRE_MILLISECONDS = 1 * ONE_HOUR_MILLIS
        private const val REFRESH_TOKEN_EXPIRE_MILLISECONDS = 14 * ONE_DAY_MILLIS
    }
}

data class AccessTokenPayload(
    val exposedId: String,
)
