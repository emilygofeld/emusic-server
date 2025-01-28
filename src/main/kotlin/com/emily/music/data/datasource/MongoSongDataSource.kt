package com.emily.music.data.datasource

import com.emily.core.constants.ID
import com.emily.music.data.entity.SongEntity
import com.emily.music.data.mappers.toSong
import com.emily.music.data.mappers.toSongEntity
import com.emily.music.domain.datasource.SongDataSource
import com.emily.music.domain.models.Song
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoSongDataSource(
    db: CoroutineDatabase
): SongDataSource {

    private val songs = db.getCollection<SongEntity>(collectionName = "Songs")

    override suspend fun getSong(songId: ID): Song? {
        return songs.findOne(SongEntity::id eq songId)?.toSong()
    }

    override suspend fun updateSong(song: Song): Boolean {
        return songs.replaceOne(SongEntity::id eq song.id, song.toSongEntity()).wasAcknowledged()
    }
}