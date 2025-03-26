package com.example.kotlin.chat.controller

import com.example.kotlin.chat.model.MessageVM
import com.example.kotlin.chat.service.MessageService
import kotlinx.coroutines.flow.*
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
@MessageMapping("api.v1.messages")
class MessageResourceController(val messageService: MessageService) {

    @MessageMapping("stream")
    suspend fun receive(@Payload inboundMessages: Flow<MessageVM>) =
        messageService.post(inboundMessages)

    @MessageMapping("stream.{gameId}")
    suspend fun send(@DestinationVariable gameId: String): Flow<MessageVM> = messageService
        .stream()
        .onStart {
            emitAll(messageService.latest(gameId))
        }



}
