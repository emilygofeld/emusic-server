package com.emily.music.data.mappers

import com.emily.music.data.entity.UserDataEntity
import com.emily.music.domain.models.UserData

fun UserDataEntity.toUserData(): UserData {
    return UserData(
        playlists = playlists,
        id = id
    )
}

fun UserData.toUserDataEntity(): UserDataEntity {
    return UserDataEntity(
        playlists = playlists,
        id = id
    )
}