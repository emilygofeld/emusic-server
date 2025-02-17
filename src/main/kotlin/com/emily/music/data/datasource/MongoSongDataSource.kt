package com.emily.music.data.datasource

import com.emily.core.constants.ID
import com.emily.music.data.entity.SongEntity
import com.emily.music.domain.datasource.SongDataSource
import com.mongodb.client.model.Filters
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoSongDataSource(
    db: CoroutineDatabase
): SongDataSource {

    private val songs = db.getCollection<SongEntity>(collectionName = "Songs")

    override suspend fun getSong(songId: ID): SongEntity? {
        return songs.findOne(SongEntity::id eq songId)
    }

    override suspend fun getSongsBySearch(search: String): List<SongEntity> {
        val searchList = mutableSetOf<SongEntity>()

        searchList.addAll(songs.find(SongEntity::title eq search).toList())

        val filter = Filters.regex("title", ".*$search.*", "i") // "i" for case-insensitive
        searchList.addAll(songs.find(filter).toList())

        return searchList.toList()
    }
}