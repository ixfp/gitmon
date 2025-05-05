package com.ixfp.gitmon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableFeignClients
@ConfigurationPropertiesScan
@EnableAspectJAutoProxy
class GitmonApplication

fun main(args: Array<String>) {
    runApplication<GitmonApplication>(*args)
}
