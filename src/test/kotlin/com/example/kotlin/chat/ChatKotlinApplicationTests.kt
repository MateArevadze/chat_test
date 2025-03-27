package com.example.kotlin.chat

import app.cash.turbine.test
import com.example.kotlin.chat.model.Message
import com.example.kotlin.chat.model.MessageDTO
import com.example.kotlin.chat.repository.MessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.dataWithType
import org.springframework.messaging.rsocket.retrieveFlow
import java.net.URI
import java.time.Instant
import java.time.temporal.ChronoUnit.MILLIS
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.r2dbc.url=r2dbc:postgresql://localhost:5432/mydatabase",
        "spring.r2dbc.username=myuser",
        "spring.r2dbc.password=mypassword",
    ]
)
class ChatKotlinApplicationTests(
    @Autowired val rsocketBuilder: RSocketRequester.Builder,
    @Autowired val messageRepository: MessageRepository,
    @LocalServerPort val serverPort: Int
) {

    lateinit var lastMessageId: String

    val now: Instant = Instant.now()

    @BeforeEach
    fun setUp() {
        runBlocking {
            val secondBeforeNow = now.minusSeconds(1)
            val twoSecondBeforeNow = now.minusSeconds(2)
            val savedMessages = messageRepository.saveAll(
                listOf(
                    Message(
                        "user1",
                        "test_game1",
                        "*testMessage*",
                        twoSecondBeforeNow,
                    ),
                    Message(
                        "user2",
                        "test_game2",
                        "**testMessage2**",
                        secondBeforeNow,
                    ),
                    Message(
                        "user3",
                        "test_game1",
                        "`testMessage3`",
                        now,
                    )
                )
            ).toList()
            lastMessageId = savedMessages.first().id ?: ""
        }
    }

    @AfterEach
    fun tearDown() {
        runBlocking {
            messageRepository.deleteAll()
        }
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test
    fun `test that messages API streams latest messages`() {
        runBlocking {
            val rSocketRequester = rsocketBuilder.websocket(URI("ws://localhost:${serverPort}/rsocket"))

            rSocketRequester
                .route("api.v1.messages.stream.{gameId}", "test_game1")
                .retrieveFlow<MessageDTO>()
                .test {
                    assertThat(expectItem().prepareForTesting())
                        .isEqualTo(
                            MessageDTO(
                                "*testMessage*",
                                "test_game1",
                                "user1",
                                now.minusSeconds(2).truncatedTo(MILLIS)
                            )
                        )

                    assertThat(expectItem().prepareForTesting())
                        .isEqualTo(
                            MessageDTO(
                                "`testMessage3`",
                                "test_game1",
                                "user3",
                                now.truncatedTo(MILLIS)
                            )
                        )

                    expectNoEvents()

                    launch {
                        rSocketRequester.route("api.v1.messages.stream")
                            .dataWithType(flow {
                                emit(
                                    MessageDTO(
                                        "`HelloWorld`",
                                        "test_game1",
                                        "test",
                                        now.plusSeconds(1)
                                    )
                                )
                            })
                            .retrieveFlow<Void>()
                            .collect()
                    }

                   assertThat(expectItem().prepareForTesting())
                        .isEqualTo(
                            MessageDTO(
                                "`HelloWorld`",
                                "test_game1",
                                "test",
                                now.plusSeconds(1).truncatedTo(MILLIS)
                            )
                        )

                    cancelAndIgnoreRemainingEvents()
                }
        }
    }

    @ExperimentalTime
    @Test
    fun `test that messages streamed to the API is stored`() {
        runBlocking {
            launch {
                val rSocketRequester = rsocketBuilder.websocket(URI("ws://localhost:${serverPort}/rsocket"))

                rSocketRequester.route("api.v1.messages.stream")
                    .dataWithType(flow {
                        emit(
                            MessageDTO(
                                "`HelloWorld`",
                                "test_game2",
                                "test",
                                now.plusSeconds(1)
                            )
                        )
                    })
                    .retrieveFlow<Void>()
                    .collect()
            }

            delay(2.seconds)

            messageRepository.findAll()
                .first { it.content.contains("HelloWorld") }
                .apply {
                    assertThat(this.prepareForTesting())
                        .isEqualTo(
                            Message(
                                "test",
                                "test_game2",
                                "`HelloWorld`",
                                now.plusSeconds(1).truncatedTo(MILLIS),
                            )
                        )
                }
        }
    }
}
