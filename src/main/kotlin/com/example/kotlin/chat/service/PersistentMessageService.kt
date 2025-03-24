package com.example.kotlin.chat.service

import com.example.kotlin.chat.asDomainObject
import com.example.kotlin.chat.asRendered
import com.example.kotlin.chat.mapToViewModel
import com.example.kotlin.chat.model.MessageList
import com.example.kotlin.chat.model.MessageVM
import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Service

@Service
class PersistentMessageService(private val msgList : MessageList) : MessageService {

    val sender: MutableSharedFlow<MessageVM> = MutableSharedFlow()

    override  suspend fun latest(): Flow<MessageVM>  {
        return msgList.getMessages().mapToViewModel().asFlow()
    }

    override suspend fun after(messageId: String): Flow<MessageVM> {
        return msgList.getMessages().mapToViewModel().asFlow()
    }


    override suspend fun stream(): Flow<MessageVM> = sender

    override suspend fun post(messages: Flow<MessageVM>) {

    }


    override suspend fun clean() {
    }
}