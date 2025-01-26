package com.emily.music.presentation.routing

import com.emily.music.domain.repository.MusicRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.musicRouting() {
    val musicRepository: MusicRepository by inject()

    routing {
        getAndDeserializeRequest(musicRepository)
    }
}
