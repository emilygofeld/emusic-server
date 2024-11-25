package com.emily.music.domain.repository

import com.emily.core.ID
import com.emily.music.domain.models.Song

interface SongDataSource {
    suspend fun getSong(songId: ID): Song?
}