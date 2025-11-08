package com.example.demo.config

import com.example.demo.constants.Qualifiers.CPU
import com.example.demo.daos.UsersDao
import com.example.demo.service.UserService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.util.concurrent.ExecutorService


@Import(DaoBeans::class, ExecutorBeans::class)
@Configuration
open class ApiRestBeans {

    @Bean
    open fun userService(
        @Qualifier(CPU) cpuPool: ExecutorService,
        usersDao: UsersDao,
    ): UserService =
        UserService(cpuPool, usersDao)

}