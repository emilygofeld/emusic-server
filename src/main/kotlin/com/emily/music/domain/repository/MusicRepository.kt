package com.emily.music.domain.repository

import com.emily.core.ID
import com.emily.music.domain.models.Playlist
import com.emily.music.domain.models.Song
import com.emily.music.domain.models.UserData

interface MusicRepository {
    suspend fun addSongToPlaylist(songId: ID, playlistId: ID): Boolean
    suspend fun removeSongFromPlaylist(songId: ID, playlistId: ID): Boolean
    suspend fun getPlaylist(playlistId: ID): Playlist?
    suspend fun createPlaylistForUser(playlist: Playlist, userId: ID): String?
    suspend fun removePlaylistFromUser(playlistId: ID, userId: ID): Boolean
    suspend fun getUserPlaylists(userId: ID): List<Playlist>
    suspend fun getSong(songId: ID): Song?
    suspend fun getUserData(userId: ID): UserData?
}