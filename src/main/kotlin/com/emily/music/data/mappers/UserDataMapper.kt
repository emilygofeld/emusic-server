package com.emily.music.data.mappers

import com.emily.music.data.entity.UserDataEntity
import com.emily.music.domain.models.UserData

fun UserDataEntity.toUserData(): UserData {
    return UserData(
        name = name,
        playlists = playlists,
        id = id
    )
}

fun UserData.toUserDataEntity(): UserDataEntity {
    return UserDataEntity(
        name = name,
        playlists = playlists
    )
}