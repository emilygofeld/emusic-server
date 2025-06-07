package com.emily.music.domain.datasource

import com.emily.core.constants.ID
import com.emily.music.data.entity.SongEntity

interface SongDataSource {
    suspend fun getSong(songId: ID): SongEntity?
    suspend fun updateSong(song: SongEntity): Boolean
    suspend fun getSongsBySearch(search: String): List<SongEntity>
    suspend fun getGlobalFavoriteSongs(): List<SongEntity>
}