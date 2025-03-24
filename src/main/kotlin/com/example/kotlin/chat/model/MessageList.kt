package com.example.kotlin.chat.model

import org.springframework.stereotype.Component
import java.util.*

@Component
class MessageList {
    private val messages = LinkedList<Message>();
    private var maxSize = 100;


    fun setMaxSize(n : Int) {
        maxSize = n;
    }
    @Synchronized
    fun sendMessage(message : Message) {
        if (messages.size == maxSize) {
            messages.removeFirst()
        }
        messages.add(message)
    }
    fun getMessages(): List<Message> {
        return messages.toList() // Return a copy of the current messages
    }
}