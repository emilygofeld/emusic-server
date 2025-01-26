package com.emily.music.data.entity

import com.emily.core.constants.ID
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class UserDataEntity(
    val playlists: List<ID>,
    @BsonId
    val id: ID
)
