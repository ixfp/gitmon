package com.ixfp.gitmon.db.exception

import com.ixfp.gitmon.common.exception.RepositoryException

sealed class DbException(
    message: String,
    cause: Throwable? = null,
) : RepositoryException(message, cause)

class DbConnectionException(
    message: String = "데이터베이스 연결에 실패했습니다.",
    cause: Throwable? = null,
) : DbException(message, cause)

class DbQueryExecutionException(
    message: String = "SQL 쿼리 실행 중 예외가 발생했습니다.",
    cause: Throwable? = null,
) : DbException(message, cause)

class DbMappingException(
    message: String = "DB 결과를 매핑하는 데 실패했습니다.",
    cause: Throwable? = null,
) : DbException(message, cause)

class DbUnexpectedException(
    message: String = "예상치 못한 DB 관련 예외가 발생했습니다.",
    cause: Throwable? = null,
) : DbException(message, cause)
