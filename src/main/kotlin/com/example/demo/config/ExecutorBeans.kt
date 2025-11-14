package com.example.demo.config

import com.example.demo.constants.Constants.JDBC
import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit.MILLISECONDS

@Configuration
class ExecutorBeans {
    @Bean
    @Qualifier(JDBC)
    open fun dbPool(env: Environment): ExecutorService {
        val prefix = "app.db"
        return createExecutorService(env, prefix)
    }

    private fun createThreadPoolFactory(threadNameFormat: String): ThreadFactory =
        ThreadFactoryBuilder().setDaemon(true).setNameFormat(threadNameFormat).build()

    private fun createExecutorService(
        env: Environment,
        prefix: String,
    ): ThreadPoolExecutor {
        val threadCount: Int = env.getRequiredProperty("$prefix.thread-pool.size", Int::class.java)
        val blockingQueueSize: Int = env.getRequiredProperty("$prefix.thread-pool.blocking-queue-size", Int::class.java)
        val threadNameFormat: String = env.getRequiredProperty("$prefix.thread-pool.executor-name-format", String::class.java)
        return ThreadPoolExecutor(
            threadCount,
            threadCount,
            0L,
            MILLISECONDS,
            ArrayBlockingQueue(blockingQueueSize),
            createThreadPoolFactory(threadNameFormat),
        )
    }
}
