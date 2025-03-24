package com.example.kotlin.chat.service

import com.example.kotlin.chat.asDomainObject
import com.example.kotlin.chat.mapToViewModel
import com.example.kotlin.chat.model.Message
import com.example.kotlin.chat.model.MessageVM
import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersistentMessageService : MessageService {
    private val messages = LinkedList<Message>();
    private val maxSize = 500;

    val sender: MutableSharedFlow<MessageVM> = MutableSharedFlow()

    override  suspend fun latest(): Flow<MessageVM>  {
        return messages.mapToViewModel().asFlow()
    }

    override suspend fun after(messageId: String): Flow<MessageVM> {
        return messages.filter{msg -> msg.id == messageId}
                        .mapToViewModel().asFlow()
    }

    override suspend fun stream(): Flow<MessageVM> = sender

    override suspend fun post(messages: Flow<MessageVM>) {
        messages.onEach{sender.emit(it)}.collect(){
            if (this.messages.size == maxSize) {
                this.messages.removeFirst()
            }
            this.messages.add(it.asDomainObject())
        }
    }

    override suspend fun clean() {
        messages.clear()
    }
}