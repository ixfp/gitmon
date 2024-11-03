package com.ixfp.gitmon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GitmonApplication

fun main(args: Array<String>) {
    runApplication<GitmonApplication>(*args)
}
