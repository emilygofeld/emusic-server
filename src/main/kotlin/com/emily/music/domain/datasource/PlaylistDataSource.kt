package com.emily.music.domain.datasource

import com.emily.core.constants.ID
import com.emily.music.data.entity.PlaylistEntity
import com.emily.music.domain.models.Playlist

interface PlaylistDataSource {
    suspend fun insertPlaylist(playlist: Playlist): String?
    suspend fun getPlaylist(id: ID): PlaylistEntity?
    suspend fun updatePlaylist(playlist: Playlist): Boolean
    suspend fun removePlaylist(playlistId: ID): Boolean
    suspend fun getUserFavoritesPlaylists(userId: ID): PlaylistEntity?
}