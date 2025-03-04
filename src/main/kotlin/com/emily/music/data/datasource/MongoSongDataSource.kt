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

        // search for song titles that match search
        val titleFilter = Filters.regex("title", "^$search", "i")
        searchList.addAll(songs.find(titleFilter).toList())

        // search for songs where any artist matches the search
        val artistFilter = Filters.regex("artists", "^$search", "i")
        searchList.addAll(songs.find(artistFilter).toList())

        return searchList.toList()
    }
}