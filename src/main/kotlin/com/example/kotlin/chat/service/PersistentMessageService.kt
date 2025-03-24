package com.example.kotlin.chat.service


import com.example.kotlin.chat.model.MessageVM
import com.example.kotlin.chat.toAsterisks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersistentMessageService() : MessageService {
    private val maxSize = 100
    val sender: MutableSharedFlow<MessageVM> = MutableSharedFlow(replay = maxSize,
                                                    extraBufferCapacity = maxSize)

    override  suspend fun latest(): Flow<MessageVM>  {
        return sender.replayCache.asFlow()
    }
    override suspend fun stream(): Flow<MessageVM> = sender

    override suspend fun post(messages: Flow<MessageVM>) {
        messages
            .collect(){
                it.user.name = it.user.name.toAsterisks()
                sender.emit(it)
        }
    }

    override suspend fun clean() {
        sender.resetReplayCache();
    }
}