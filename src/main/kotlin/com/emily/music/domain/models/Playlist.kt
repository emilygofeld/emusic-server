package com.emily.music.domain.models

import com.emily.core.ID

data class Playlist(
    val title: String,
    val songs: List<ID>,
    val ownerId: ID,
    val id: ID
)

