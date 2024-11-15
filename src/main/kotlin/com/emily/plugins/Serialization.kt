package com.emily.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import com.emily.auth.presentation.auth.json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(json)
    }
}
