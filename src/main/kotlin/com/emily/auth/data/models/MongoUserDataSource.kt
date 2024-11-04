package com.emily.auth.data.models

import com.emily.auth.data.entity.UserEntity
import com.emily.auth.data.mappers.toUser
import com.emily.auth.data.mappers.toUserEntity
import com.emily.auth.domain.models.User
import com.emily.auth.domain.models.UserDataSource
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataSource(
    db: CoroutineDatabase
): UserDataSource {

    private val users = db.getCollection<UserEntity>(collectionName = "Users")

    override suspend fun getUserByUsername(username: String): User? {
        return users.findOne(UserEntity::username eq username)?.toUser()
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user.toUserEntity()).wasAcknowledged()
    }
}
