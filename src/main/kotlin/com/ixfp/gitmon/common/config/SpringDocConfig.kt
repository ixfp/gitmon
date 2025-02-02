package com.ixfp.gitmon.common.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

data class ResponseMsg(
    val code: String, val message: String, val timestamp: String = "2025-01-01T12:00:00Z"
)

@Configuration
// TODO(KHJ): 인증 관련 처리 필요함
class SpringDocConfig {
    // SpringDoc Main Title 세팅
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI().info(configurationInfo()).components(Components())
    }

    private fun configurationInfo(): Info {
        return Info().title("Gitmon API Documentation").description("Gitmon 통합 API 정리 문서").version("0.1.0")
    }

    // 전역 Response 수정
    @Bean
    fun openAPI(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi: OpenAPI ->
            openApi.paths.orEmpty().values.forEach { pathItem ->
                pathItem.readOperations().forEach { operation ->
                    val apiResponses: ApiResponses = operation.responses

                    // 미지원 Standard Response 추가
                    apiResponses.addApiResponse("400", customApiResponse("400", "Bad Request"))
                    apiResponses.addApiResponse("500", customApiResponse("500", "Internal Server Error"))
                }
            }
        }
    }

    private fun customApiResponse(opcode: String, description: String): ApiResponse {
        val exampleMsg = ResponseMsg(
            code = opcode,
            message = description,
        )

        return ApiResponse().apply {
            this.description = description
            this.content = Content().apply {
                addMediaType(
                    org.springframework.http.MediaType.APPLICATION_JSON_VALUE, MediaType().apply {
                        addExamples("example", Example().apply {
                            value = exampleMsg
                        })
                        schema = Schema<Any>()
                    })
            }
        }
    }
}
