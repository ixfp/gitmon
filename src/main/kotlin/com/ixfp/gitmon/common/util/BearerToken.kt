package com.ixfp.gitmon.common.util

data class BearerToken(
    val token: String,
) {
    fun format(): String {
        return "Bearer $token"
    }
}
