package com.ixfp.gitmon.db.exception

import com.ixfp.gitmon.common.aop.ExceptionWrappingStrategy
import com.ixfp.gitmon.common.exception.AppException
import jakarta.persistence.PersistenceException
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.stereotype.Component
import java.sql.SQLException
import org.springframework.dao.DataAccessException as SpringDataAccessException

@Component
class DbExceptionStrategy : ExceptionWrappingStrategy {
    override fun wrap(e: Throwable): AppException =
        when (e) {
            is AppException -> e
            is DataAccessResourceFailureException,
            is SQLException,
            -> DbConnectionException(cause = e)
            is SpringDataAccessException -> DbQueryExecutionException(cause = e)
            is PersistenceException -> DbMappingException(cause = e)
            else -> DbUnexpectedException(cause = e)
        }
}
