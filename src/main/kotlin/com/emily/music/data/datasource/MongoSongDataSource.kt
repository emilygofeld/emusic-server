package com.emily.music.data.datasource

import com.emily.core.constants.ID
import com.emily.music.data.entity.SongEntity
import com.emily.music.domain.datasource.SongDataSource
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoSongDataSource(
    db: CoroutineDatabase
): SongDataSource {

    private val songs = db.getCollection<SongEntity>(collectionName = "Songs")

    override suspend fun getSong(songId: ID): SongEntity? {
        return songs.findOne(SongEntity::id eq songId)
    }
}