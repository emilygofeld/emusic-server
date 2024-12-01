package com.emily.music.domain.datasource

import com.emily.core.ID
import com.emily.music.domain.models.Playlist

interface PlaylistDataSource {
    suspend fun insertPlaylist(playlist: Playlist): String?
    suspend fun getPlaylist(id: ID): Playlist?
    suspend fun updatePlaylist(playlist: Playlist): Boolean
    suspend fun removePlaylist(playlistId: ID): Boolean
}