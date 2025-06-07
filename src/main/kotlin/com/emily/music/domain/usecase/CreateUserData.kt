package com.emily.music.domain.usecase

import com.emily.core.UserCreationObserver
import com.emily.music.domain.models.Playlist
import com.emily.music.domain.repository.MusicRepository

object UserCreationListener {
    suspend fun listen(musicRepository: MusicRepository) {
        UserCreationObserver.events.collect { event ->
            musicRepository.insertUserData(event.userId)

            val playlist = Playlist(
                title = "Favorites",
                ownerName = event.username,
                ownerId = event.userId
            )
            musicRepository.createPlaylistForUser(playlist, event.userId)
        }
    }
}