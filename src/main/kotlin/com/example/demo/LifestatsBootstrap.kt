package com.example.demo

import com.example.demo.controller.CommonController
import com.example.demo.security.SecurityConfig
import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration

@Configuration
@EnableAutoConfiguration
open class LifestatsBootstrap {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val logger = LoggerFactory.getLogger(LifestatsBootstrap::class.java)

            try {
                val ctx = SpringApplicationBuilder(LifestatsBootstrap::class.java)
                    .bannerMode(Banner.Mode.OFF)
                    .headless(true)
                    .logStartupInfo(true)
                    .sources(
                        CommonController::class.java,
                        SecurityConfig::class.java,
                    )
                    .build()
                    .run(*args)
            } catch (ex: Exception) {
                logger.error("Error starting Lifestats application", ex.message)
            }
        }
    }
}
