package com.example.kotlin.chat.service


import com.example.kotlin.chat.asDomainObject
import com.example.kotlin.chat.mapToViewModel
import com.example.kotlin.chat.model.MessageVM
import com.example.kotlin.chat.repository.MessageRepository
import com.example.kotlin.chat.toAsterisks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PersistentMessageService(private val messageRepository : MessageRepository)
    : MessageService {

    val logger = LoggerFactory.getLogger(PersistentMessageService::class.java)
    private val maxSize = 3
    val sender: MutableSharedFlow<MessageVM> = MutableSharedFlow(replay = maxSize,
                                                    extraBufferCapacity = maxSize)
    override  suspend fun latest(): Flow<MessageVM>  {
        logger.info(sender.replayCache.size.toString())
        return messageRepository.findLatest().mapToViewModel()

    }
    override suspend fun stream(): Flow<MessageVM> = sender

    override suspend fun post(messages: Flow<MessageVM>) {
        messages
            .collect {
                it.user.name = it.user.name.toAsterisks()
                sender.emit(it)
                messageRepository.save(it.asDomainObject())
            }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun clean() {
        sender.resetReplayCache()
    }
}