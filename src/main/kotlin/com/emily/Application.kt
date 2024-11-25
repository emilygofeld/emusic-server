package com.emily

import com.emily.auth.di.authModule
import com.emily.auth.domain.security.token.TokenConfig
import com.emily.di.appModule
import com.emily.music.di.musicModule
import com.emily.plugins.configureMonitoring
import com.emily.plugins.configureRouting
import com.emily.plugins.configureSecurity
import com.emily.plugins.configureSerialization
import io.ktor.server.application.*
import org.koin.core.context.startKoin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("UNUSED")
fun Application.module() {
    startKoin {
        modules(
            appModule,
            authModule,
            musicModule
        )
    }

    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 30L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )

    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureRouting(tokenConfig)
}
