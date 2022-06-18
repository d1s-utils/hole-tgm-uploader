package dev.d1s.holetgmuploader.configuration

import dev.d1s.holetgmuploader.properties.TelegramBotConfigurationProperties
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TelegramBotConfiguration {

    @set:Autowired
    lateinit var telegramBotConfigurationProperties: TelegramBotConfigurationProperties

    @Bean
    fun telegramBot() = telegramBot(
        telegramBotConfigurationProperties.token
    )
}