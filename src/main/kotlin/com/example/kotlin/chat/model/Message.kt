package com.example.kotlin.chat.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("MESSAGES")
data class Message(
    val userId: String,
    val gameId: String,
    val content: String,
    val timeSent: Instant,
    @Id var id: String? = null)
