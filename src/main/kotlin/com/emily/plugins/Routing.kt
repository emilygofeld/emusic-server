package com.emily.plugins

import com.emily.auth.domain.security.token.TokenConfig
import com.emily.auth.presentation.routing.authRouting
import com.emily.music.presentation.routing.musicRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(config: TokenConfig) {
    routing {
        authRouting(config)
        musicRouting()
    }
}
