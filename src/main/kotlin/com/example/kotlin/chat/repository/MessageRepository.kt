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
            WHERE game_id = :gameId
            ORDER BY time_sent DESC
            LIMIT :maxSize
        ) as "M*" ORDER BY time_sent
    """)
    fun findLatest(maxSize:Int, gameId: String): Flow<Message>
}