package com.ixfp.gitmon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class GitmonApplication

fun main(args: Array<String>) {
    runApplication<GitmonApplication>(*args)
}
