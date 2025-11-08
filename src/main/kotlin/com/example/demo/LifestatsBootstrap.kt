package com.example.demo

import com.example.demo.config.ApiRestBeans
import com.example.demo.config.ExecutorBeans
import com.example.demo.controller.CommonController
import com.example.demo.controller.UsersController
import com.example.demo.security.SecurityConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Configuration

@Configuration
@EnableAutoConfiguration
open class LifestatsBootstrap {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val logger = LoggerFactory.getLogger(LifestatsBootstrap::class.java)

            try {
                SpringApplicationBuilder(LifestatsBootstrap::class.java)
                    .bannerMode(Banner.Mode.OFF)
                    .headless(true)
                    .logStartupInfo(true)
                    .sources(
                        ApiRestBeans::class.java,
                        CommonController::class.java,
                        ExecutorBeans::class.java,
                        SecurityConfig::class.java,
                        UsersController::class.java,
                    )
                    .build()
                    .run(*args)
            } catch (ex: Exception) {
                logger.error("Error starting Lifestats application", ex.message)
            }
        }
    }
}
