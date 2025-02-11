package com.emily.music.domain.repository

import com.emily.core.constants.ID
import com.emily.music.domain.models.Playlist
import com.emily.music.domain.models.Song
import com.emily.music.domain.models.UserData

interface MusicRepository {
    suspend fun addSongToPlaylist(songId: ID, playlistId: ID): Boolean
    suspend fun addSongToFavorites(songId: ID, userId: ID): Boolean
    suspend fun removeSongFromFavorites(songId: ID, userId: ID): Boolean
    suspend fun removeSongFromPlaylist(songId: ID, playlistId: ID): Boolean
    suspend fun getPlaylist(playlistId: ID, userId: ID): Playlist?
    suspend fun createPlaylistForUser(playlist: Playlist, userId: ID): ID?
    suspend fun removePlaylistFromUser(playlistId: ID, userId: ID): Boolean
    suspend fun getUserPlaylists(userId: ID): List<Playlist>
    suspend fun getSong(songId: ID, userId: ID): Song?
    suspend fun insertUserData(userId: ID): Boolean
    suspend fun getUserData(userId: ID): UserData?
    suspend fun updatePlaylist(playlist: Playlist): Boolean
}