package com.emily.music.data.repository

import com.emily.core.ID
import com.emily.music.domain.datasource.PlaylistDataSource
import com.emily.music.domain.models.Playlist
import com.emily.music.domain.models.Song
import com.emily.music.domain.models.UserData
import com.emily.music.domain.repository.MusicRepository
import com.emily.music.domain.repository.SongDataSource
import com.emily.music.domain.repository.UserDataDataSource

class MusicRepositoryImpl(
    private val playlistDataSource: PlaylistDataSource,
    private val userDataDataSource: UserDataDataSource,
    private val songDataSource: SongDataSource
): MusicRepository {
    override suspend fun addSongToPlaylist(songId: ID, playlistId: String) {
        var playlist = playlistDataSource.getPlaylist(playlistId) ?: return
        if (playlist.songs.contains(songId)) {
            return
        }

        playlist = playlist.copy(songs = playlist.songs + songId)
        playlistDataSource.updatePlaylist(playlist)
    }

    override suspend fun removeSongFromPlaylist(songId: ID, playlistId: ID) {
        var playlist = playlistDataSource.getPlaylist(playlistId) ?: return
        if (playlist.songs.contains(songId)) {
            return
        }
        playlist = playlist.copy(songs = playlist.songs - songId)
        playlistDataSource.updatePlaylist(playlist)
    }

    override suspend fun getPlaylist(playlistId: ID): Playlist? {
        return playlistDataSource.getPlaylist(playlistId)
    }

    override suspend fun createPlaylistForUser(playlist: Playlist, userId: ID) {
        var userData = userDataDataSource.getUserData(userId) ?: return
        playlistDataSource.insertPlaylist(playlist)

        userData = userData.copy(playlists = userData.playlists + playlist.id)
        userDataDataSource.updateUserData(userData)
    }

    override suspend fun removePlaylistFromUser(playlistId: ID, userId: ID) {
        var userData = userDataDataSource.getUserData(userId) ?: return
        playlistDataSource.removePlaylist(playlistId)

        userData = userData.copy(playlists = userData.playlists - playlistId)
        userDataDataSource.updateUserData(userData)
    }

    override suspend fun getSong(songId: ID): Song? {
        return songDataSource.getSong(songId)
    }

    override suspend fun getUserData(userId: ID): UserData? {
        return userDataDataSource.getUserData(userId)
    }
}

