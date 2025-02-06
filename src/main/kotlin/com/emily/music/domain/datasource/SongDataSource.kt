package com.emily.music.domain.datasource

import com.emily.core.constants.ID
import com.emily.music.data.entity.SongEntity

interface SongDataSource {
    suspend fun getSong(songId: ID): SongEntity?
}