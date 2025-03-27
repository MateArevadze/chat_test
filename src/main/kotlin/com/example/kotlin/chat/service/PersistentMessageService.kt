package com.example.kotlin.chat.service


import com.example.kotlin.chat.model.MessageDTO
import com.example.kotlin.chat.model.asDomainObject
import com.example.kotlin.chat.model.mapToDTO
import com.example.kotlin.chat.repository.MessageRepository
import kotlinx.coroutines.flow.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PersistentMessageService(private val messageRepository : MessageRepository)
    : MessageService {

    val logger = LoggerFactory.getLogger(PersistentMessageService::class.java)
    private val maxSize = 5
    val sender: MutableSharedFlow<MessageDTO> = MutableSharedFlow()

    override  suspend fun latest(gameId: String): Flow<MessageDTO> {
        logger.info(gameId)
        return messageRepository.findLatest(maxSize, gameId)
            .mapToDTO()
    }

    override suspend fun stream(): Flow<MessageDTO> = sender

    override suspend fun post(messages: Flow<MessageDTO>) {
        messages
            .buffer(50)
            .onEach { sender.tryEmit(it) }
            .map { it.asDomainObject() }
            .let { messageRepository.saveAll(it) }
            .collect()
    }

    override suspend fun clean() {
        messageRepository.deleteAll()
    }
}