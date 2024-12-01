package com.emily.music.presentation.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.musicRouting() {

    routing {
        getAndDeserializeRequest()
    }
}