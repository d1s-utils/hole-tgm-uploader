package dev.d1s.holetgmuploader.properties

import dev.d1s.holetgmuploader.constant.TELEGRAM_BOT_PROPERTY_PREFIX
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
@ConstructorBinding
@ConfigurationProperties(TELEGRAM_BOT_PROPERTY_PREFIX)
data class TelegramBotConfigurationProperties(

    @NotBlank
    val token: String
)