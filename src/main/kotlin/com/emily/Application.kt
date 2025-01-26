package com.emily

import com.emily.auth.di.authModule
import com.emily.auth.domain.security.token.TokenConfig
import com.emily.di.appModule
import com.emily.music.di.musicModule
import com.emily.music.domain.repository.MusicRepository
import com.emily.music.domain.usecase.UserDataListener
import com.emily.plugins.configureMonitoring
import com.emily.plugins.configureRouting
import com.emily.plugins.configureSecurity
import com.emily.plugins.configureSerialization
import io.ktor.server.application.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.ktor.ext.inject

fun main(args: Array<String>) {
    startKoin {
        modules(
            appModule,
            authModule,
            musicModule
        )
    }
    io.ktor.server.netty.EngineMain.main(args)
}

@OptIn(DelicateCoroutinesApi::class)
@Suppress("UNUSED")
fun Application.module() {
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 30L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )

    val musicRepository: MusicRepository by inject()

    GlobalScope.launch {
        UserDataListener.listen(musicRepository)
    }

    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureRouting(tokenConfig)
}