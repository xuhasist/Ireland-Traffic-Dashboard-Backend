package com.itd.config

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StartupLogConfig {

    @Bean
    fun startupRunner(): CommandLineRunner {
        return CommandLineRunner {
            println("Spring Boot started successfully")
        }
    }
}