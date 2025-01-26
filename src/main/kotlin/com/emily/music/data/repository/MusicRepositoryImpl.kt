package com.emily.music.data.repository

import com.emily.core.constants.ID
import com.emily.music.domain.datasource.PlaylistDataSource
import com.emily.music.domain.models.Playlist
import com.emily.music.domain.models.Song
import com.emily.music.domain.models.UserData
import com.emily.music.domain.repository.MusicRepository
import com.emily.music.domain.datasource.SongDataSource
import com.emily.music.domain.datasource.UserDataDataSource

class MusicRepositoryImpl(
    private val playlistDataSource: PlaylistDataSource,
    private val userDataDataSource: UserDataDataSource,
    private val songDataSource: SongDataSource
): MusicRepository {
    override suspend fun addSongToPlaylist(songId: ID, playlistId: String): Boolean {
        var playlist = playlistDataSource.getPlaylist(playlistId) ?: return false
        if (playlist.songs.contains(songId)) {
            return false
        }

        playlist = playlist.copy(songs = playlist.songs + songId)
        return playlistDataSource.updatePlaylist(playlist)
    }

    override suspend fun removeSongFromPlaylist(songId: ID, playlistId: ID): Boolean {
        var playlist = playlistDataSource.getPlaylist(playlistId) ?: return false
        if (playlist.songs.contains(songId)) {
            return false
        }
        playlist = playlist.copy(songs = playlist.songs - songId)
        return playlistDataSource.updatePlaylist(playlist)
    }

    override suspend fun getPlaylist(playlistId: ID): Playlist? {
        return playlistDataSource.getPlaylist(playlistId)
    }

    override suspend fun createPlaylistForUser(playlist: Playlist, userId: ID): String? {
        var userData = userDataDataSource.getUserData(userId)
        if (userData == null) // if userData doesn't exist yet
            if (!userDataDataSource.insertUserData(userId)) return null // if there was an error inserting

        userData = userDataDataSource.getUserData(userId)?: return null
        val playlistId = playlistDataSource.insertPlaylist(playlist) ?: return null

        userData = userData.copy(playlists = userData.playlists + playlistId)
        return if(userDataDataSource.updateUserData(userData)) playlistId else null
    }

    override suspend fun removePlaylistFromUser(playlistId: ID, userId: ID): Boolean {
        var userData = userDataDataSource.getUserData(userId) ?: return false
        if (!playlistDataSource.removePlaylist(playlistId)) return false

        userData = userData.copy(playlists = userData.playlists - playlistId)
        return userDataDataSource.updateUserData(userData)
    }

    override suspend fun getUserPlaylists(userId: ID): List<Playlist> {
        val userData = userDataDataSource.getUserData(userId) ?: return emptyList()
        var playlists = emptyList<Playlist>()
        userData.playlists.forEach { id ->
            playlistDataSource.getPlaylist(id)?.let { playlist ->
                playlists += playlist
            }
        }
        return playlists
    }

    override suspend fun getSong(songId: ID): Song? {
        return songDataSource.getSong(songId)
    }

    override suspend fun insertUserData(userId: ID): Boolean {
        return userDataDataSource.insertUserData(userId)
    }

    override suspend fun getUserData(userId: ID): UserData? {
        return userDataDataSource.getUserData(userId)
    }
}

