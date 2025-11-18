package com.example.demo.config

import com.example.demo.daos.EventsDao
import com.example.demo.daos.UserRepository
import com.example.demo.daos.UsersDao
import com.example.demo.service.EventsService
import com.example.demo.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(DaoBeans::class, ExecutorBeans::class)
@Configuration
open class ApiRestBeans {
    @Bean
    open fun userService(
        usersDao: UsersDao,
        userRepository: UserRepository,
    ): UserService = UserService(usersDao, userRepository)

    @Bean
    open fun eventsService(eventsDao: EventsDao): EventsService = EventsService(eventsDao)
}
