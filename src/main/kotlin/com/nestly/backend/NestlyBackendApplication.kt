package com.nestly.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NestlyBackendApplication

fun main(args: Array<String>) {
    runApplication<NestlyBackendApplication>(*args)
}
