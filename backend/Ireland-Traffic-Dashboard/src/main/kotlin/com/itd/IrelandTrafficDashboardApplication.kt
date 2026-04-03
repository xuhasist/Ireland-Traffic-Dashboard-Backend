package com.itd

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class IrelandTrafficDashboardApplication

fun main(args: Array<String>) {
    runApplication<IrelandTrafficDashboardApplication>(*args)

    /*
    PostgreSQL：cities / city config structured data
    Redis：third-party API cache
    MongoDB：dashboard snapshot / history
    */
}