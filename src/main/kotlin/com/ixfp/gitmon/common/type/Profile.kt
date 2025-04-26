package com.ixfp.gitmon.common.type

enum class Profile(val code: String, val description: String) {
    DEV("dev", "개발환경"),
    PROD("prod", "운영환경"),
    ;

    companion object {
        fun findByCode(code: String?): Profile {
            return entries.find { it.code == code } ?: PROD
        }
    }
}
