package com.emily.music.domain.datasource

import com.emily.core.ID
import com.emily.music.domain.models.UserData

interface UserDataDataSource {
    suspend fun getUserData(userId: ID): UserData?
    suspend fun updateUserData(userData: UserData): Boolean
}