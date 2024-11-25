package com.emily.music.data.entity

import com.emily.core.ID
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class UserDataEntity(
    val name: String,
    val playlists: List<ID>,
    @BsonId
    val id: ID = ObjectId().toString()
)
