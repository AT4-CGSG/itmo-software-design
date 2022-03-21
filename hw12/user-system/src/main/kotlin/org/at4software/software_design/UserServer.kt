package org.at4software.software_design

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.util.*

/**
 * @author Tsutsiev Andrey
 */
@SpringBootApplication
open class UserServer

fun main(args: Array<String>) {
    SpringApplication(UserServer::class.java).run {
        setDefaultProperties(Collections.singletonMap<String?, Any?>("server.port", "8081"))
        run(*args)
    }
}