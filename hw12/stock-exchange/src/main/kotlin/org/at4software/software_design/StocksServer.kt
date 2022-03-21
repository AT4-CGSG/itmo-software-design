package org.at4software.software_design

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.util.Collections.singletonMap

/**
 * @author Tsutsiev Andrey
 */
@SpringBootApplication
open class StocksServer

fun main(args: Array<String>) {
    SpringApplication(StocksServer::class.java).run {
        setDefaultProperties(singletonMap<String, Any>("server.port", "8080"))
        run(*args)
    }
}
