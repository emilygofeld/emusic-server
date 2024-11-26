package com.emily.music.domain.models

import com.emily.core.ID
import kotlinx.serialization.Serializable

@Serializable
data class Playlist(
    val title: String,
    val songs: List<ID>,
    val ownerId: ID,
    val id: ID
)

