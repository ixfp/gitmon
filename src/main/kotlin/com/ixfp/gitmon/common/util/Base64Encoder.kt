package com.ixfp.gitmon.common.util

import org.springframework.web.multipart.MultipartFile
import java.util.Base64

object Base64Encoder {
    fun encodeBase64(file: MultipartFile): String {
        return Base64.getEncoder().encodeToString(file.bytes)
    }
}
