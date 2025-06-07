package com.emily.music.domain.models

import com.emily.core.constants.ID
import kotlinx.serialization.Serializable

@Serializable
class Song(
    val title: String,
    val artists: List<String>,
    val length: Int,
    var isFavorite: Boolean = false,
    val url: String = "",
    var favoriteCount: Int = 0,
    val id: ID = ""
)
