package com.ixfp.gitmon.common.exception

open class PresentationException(message: String, cause: Throwable? = null) : AppException(message, cause)

open class DomainException(message: String, cause: Throwable? = null) : AppException(message, cause)

open class RepositoryException(message: String, cause: Throwable? = null) : AppException(message, cause)
