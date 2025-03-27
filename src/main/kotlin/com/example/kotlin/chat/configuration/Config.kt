package com.example.kotlin.chat.configuration

import io.r2dbc.spi.ConnectionFactory
import io.rsocket.transport.netty.server.TcpServerTransport
import org.springframework.boot.rsocket.server.RSocketServer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import java.time.Duration

@Configuration
class Config {
    @Bean
    fun rd2bcInitializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        val populator = CompositeDatabasePopulator()
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("./sql/schema.sql")))
        initializer.setDatabasePopulator(populator)
        return initializer
    }


}