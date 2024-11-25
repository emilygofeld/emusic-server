package com.emily.music.domain.models

import com.emily.core.ID

data class Song(
    val title: String,
    val artists: List<String>,
    val length: Int,
    val id: ID
)
