package com.emily.music.domain.models

import com.emily.core.ID

data class UserData(
    val name: String,
    val playlists: List<ID>,
    val id: ID
)
