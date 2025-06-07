package com.emily.core

import com.emily.core.constants.ID
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

object UserCreationObserver {
    private val _events = Channel<UserCreatedEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: UserCreatedEvent) {
        _events.send(event)
    }
}

data class UserCreatedEvent(val userId: ID, val username: String)
