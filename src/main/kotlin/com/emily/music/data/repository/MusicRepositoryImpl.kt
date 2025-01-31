package com.emily.music.data.repository

import com.emily.core.constants.ID
import com.emily.music.data.entity.PlaylistEntity
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
    override suspend fun addSongToPlaylist(songId: ID, playlistId: ID): Boolean {
        val playlistEntity = playlistDataSource.getPlaylist(playlistId) ?: return false

        if (playlistEntity.songs.any { id -> id == songId }) {
            return false
        }
        val songToAdd = songDataSource.getSong(songId) ?: return false

        var playlist = convertEntityToPlaylist(playlistEntity)
        playlist = playlist.copy(songs = playlist.songs + songToAdd)

        return playlistDataSource.updatePlaylist(playlist)
    }

    override suspend fun addSongToFavorites(songId: ID, userId: ID): Boolean {
        val favoritesPlaylist = playlistDataSource.getUserFavoritesPlaylists(userId) ?: return false
        val song = songDataSource.getSong(songId) ?: return false

        song.isFavorite = true
        songDataSource.updateSong(song)

        return addSongToPlaylist(songId, favoritesPlaylist.id)
    }

    override suspend fun removeSongFromFavorites(songId: ID, userId: ID): Boolean {
        val favoritesPlaylistEntity = playlistDataSource.getUserFavoritesPlaylists(userId) ?: return false
        val favoritesPlaylist = convertEntityToPlaylist(favoritesPlaylistEntity)

        val song = favoritesPlaylist.songs.find { song -> song.id == songId} ?: return false
        song.isFavorite = false
        songDataSource.updateSong(song)

        return removeSongFromPlaylist(songId, favoritesPlaylist.id)
    }

    override suspend fun removeSongFromPlaylist(songId: ID, playlistId: ID): Boolean {
        var playlistEntity = playlistDataSource.getPlaylist(playlistId) ?: return false
        if (!playlistEntity.songs.any { id -> id == songId }) {
            return false
        }
        val updatedSongs = playlistEntity.songs.filter { id -> id != songId }
        playlistEntity = playlistEntity.copy(songs = updatedSongs)
        return playlistDataSource.updatePlaylist(convertEntityToPlaylist(playlistEntity))
    }

    override suspend fun getPlaylist(playlistId: ID): Playlist? {
        val playlist = playlistDataSource.getPlaylist(playlistId) ?: return null
        return convertEntityToPlaylist(playlist)
    }

    override suspend fun createPlaylistForUser(playlist: Playlist, userId: ID): ID? {
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
        val playlists = mutableListOf<Playlist>()
        userData.playlists.forEach { id ->
            playlistDataSource.getPlaylist(id)?.let { playlistEntity ->
                val playlist = convertEntityToPlaylist(playlistEntity)
                playlists.add(playlist)
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

    private suspend fun convertEntityToPlaylist(entity: PlaylistEntity): Playlist {
        var playlist = Playlist(
            title = entity.title,
            ownerName = entity.ownerName,
            ownerId = entity.ownerId,
            id = entity.id
        )

        val songs = mutableListOf<Song>()
        for (songId in entity.songs) {
            val song = songDataSource.getSong(songId)
            if (song != null) songs.add(song)
        }

        playlist = playlist.copy(songs = songs)
        return playlist
    }
}

