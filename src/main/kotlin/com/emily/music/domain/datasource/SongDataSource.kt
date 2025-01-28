package com.emily.music.domain.datasource

import com.emily.core.constants.ID
import com.emily.music.domain.models.Song

interface SongDataSource {
    suspend fun getSong(songId: ID): Song?
    suspend fun updateSong(song: Song): Boolean
}