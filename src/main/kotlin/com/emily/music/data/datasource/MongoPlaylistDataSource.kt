package com.emily.music.data.datasource

import com.emily.core.constants.ID
import com.emily.music.data.entity.PlaylistEntity
import com.emily.music.data.mappers.toPlaylistEntity
import com.emily.music.domain.datasource.PlaylistDataSource
import com.emily.music.domain.models.Playlist
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq


class MongoPlaylistDataSource(
    db: CoroutineDatabase
): PlaylistDataSource {

    private val playlists = db.getCollection<PlaylistEntity>(collectionName = "Playlists")

    override suspend fun insertPlaylist(playlist: Playlist): String? {
        val entity = playlist.toPlaylistEntity()
        return if (playlists.insertOne(entity).wasAcknowledged()) entity.id else null
    }

    override suspend fun getPlaylist(id: ID): PlaylistEntity? {
        return playlists.findOne(PlaylistEntity::id eq id)
    }

    override suspend fun updatePlaylist(playlistEntity: PlaylistEntity): Boolean {
        return playlists.replaceOne(PlaylistEntity::id eq playlistEntity.id, playlistEntity).wasAcknowledged()
    }

    override suspend fun removePlaylist(playlistId: ID): Boolean {
        return playlists.deleteOne(PlaylistEntity::id eq playlistId).wasAcknowledged()
    }

    override suspend fun getUserFavoritesPlaylists(userId: ID): PlaylistEntity? {
        return playlists.findOne(and(PlaylistEntity::ownerId eq userId, PlaylistEntity::title eq "Favorites"))
    }
}