package com.emily.music.data.repository

import com.emily.core.constants.ID
import com.emily.music.data.entity.PlaylistEntity
import com.emily.music.data.mappers.toPlaylistEntity
import com.emily.music.data.mappers.toSong
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
        var playlistEntity = playlistDataSource.getPlaylist(playlistId) ?: return false
        if (playlistEntity.songs.any { id -> id == songId }) {
            return false
        }

        playlistEntity = playlistEntity.copy(songs = playlistEntity.songs + songId)
        return playlistDataSource.updatePlaylist(playlistEntity)
    }

    override suspend fun addSongToFavorites(songId: ID, userId: ID): Boolean {
        val favoritesPlaylistEntity = playlistDataSource.getUserFavoritesPlaylists(userId) ?: return false
        return addSongToPlaylist(songId, favoritesPlaylistEntity.id)
    }

    override suspend fun removeSongFromFavorites(songId: ID, userId: ID): Boolean {
        val favoritesPlaylistEntity = playlistDataSource.getUserFavoritesPlaylists(userId) ?: return false
        return removeSongFromPlaylist(songId, favoritesPlaylistEntity.id)
    }

    override suspend fun removeSongFromPlaylist(songId: ID, playlistId: ID): Boolean {
        var playlistEntity = playlistDataSource.getPlaylist(playlistId) ?: return false
        if (!playlistEntity.songs.any { id -> id == songId }) {
            return false
        }

        val updatedSongs = playlistEntity.songs.filter { id -> id != songId }
        playlistEntity = playlistEntity.copy(songs = updatedSongs)
        return playlistDataSource.updatePlaylist(playlistEntity)
    }

    override suspend fun getPlaylist(playlistId: ID, userId: ID): Playlist? {
        val playlist = playlistDataSource.getPlaylist(playlistId) ?: return null
        return convertEntityToPlaylist(playlist, userId)
    }

    override suspend fun createPlaylistForUser(playlist: Playlist, userId: ID): ID? {
        var userData = userDataDataSource.getUserData(userId)
        if (userData == null)
            if (!userDataDataSource.insertUserData(userId)) return null

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
                val playlist = convertEntityToPlaylist(playlistEntity, userId)
                playlists.add(playlist)
            }
        }
        return playlists
    }

    override suspend fun getSong(songId: ID, userId: ID): Song? {
        return songDataSource.getSong(songId)?.toSong(isSongFavorite(songId, userId))
    }

    override suspend fun insertUserData(userId: ID): Boolean {
        return userDataDataSource.insertUserData(userId)
    }

    override suspend fun getUserData(userId: ID): UserData? {
        return userDataDataSource.getUserData(userId)
    }

    override suspend fun updatePlaylist(playlist: Playlist): Boolean {
        return playlistDataSource.updatePlaylist(playlist.toPlaylistEntity())
    }


    private suspend fun convertEntityToPlaylist(entity: PlaylistEntity, userId: ID): Playlist {
        var playlist = Playlist(
            title = entity.title,
            ownerName = entity.ownerName,
            ownerId = entity.ownerId,
            id = entity.id
        )

        val songs = mutableListOf<Song>()
        for (songId in entity.songs) {
            val songEntity = songDataSource.getSong(songId)
            if (songEntity != null) songs.add(songEntity.toSong(isFavorite = isSongFavorite(songId, userId)))
        }

        playlist = playlist.copy(songs = songs)
        return playlist
    }

    private suspend fun isSongFavorite (songId: ID, userId: ID): Boolean {
        val favoritesPlaylist = playlistDataSource.getUserFavoritesPlaylists(userId) ?: return false
        return favoritesPlaylist.songs.contains(songId)
    }
}

