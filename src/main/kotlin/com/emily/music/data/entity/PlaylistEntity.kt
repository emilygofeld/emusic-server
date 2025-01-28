package com.emily.music.data.entity

import com.emily.core.constants.ID
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class PlaylistEntity(
    val title: String,
    val songs: List<SongEntity>,
    val ownerName: String,
    val ownerId: ID,
    @BsonId
    val id: ID = ObjectId().toString()
)
