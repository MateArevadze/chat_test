package com.example.kotlin.chat.repository

import com.example.kotlin.chat.model.Message
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface MessageRepository : CoroutineCrudRepository<Message, String> {

    // language=SQL
    @Query("""
        SELECT * FROM (
            SELECT * FROM MESSAGES
            ORDER BY sent DESC
            LIMIT 10
        ) as "M*" ORDER BY sent
    """)
    fun findLatest(): Flow<Message>
}