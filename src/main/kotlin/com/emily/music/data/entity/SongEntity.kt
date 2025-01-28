package com.emily.music.data.entity

import com.emily.core.constants.ID
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class SongEntity(
    val title: String,
    val artists: List<String>,
    val length: Int,
    val isFavorite: Boolean = false,
    val url: String,
    @BsonId
    val id: ID = ObjectId().toString()
)
