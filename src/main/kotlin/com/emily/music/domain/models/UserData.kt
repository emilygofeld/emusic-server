package com.emily.music.domain.models

import com.emily.core.constants.ID
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val playlists: List<ID> = emptyList(),
    val id: ID
)
