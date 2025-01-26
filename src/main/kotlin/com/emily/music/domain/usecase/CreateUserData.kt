package com.emily.music.domain.usecase

import com.emily.core.UserDataObserver
import com.emily.music.domain.repository.MusicRepository

object UserDataListener {
    suspend fun listen(musicRepository: MusicRepository) {
        UserDataObserver.eventFlow.collect { event ->
            musicRepository.insertUserData(event.userId)
        }
    }
}