package dev.d1s.holetgmuploader.service.impl

import dev.d1s.holetgmuploader.handler.AttachmentHandler
import dev.d1s.holetgmuploader.service.AttachmentHandlingService
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AttachmentHandlingServiceImpl : AttachmentHandlingService {

    @set:Autowired
    lateinit var attachmentHandlers: List<AttachmentHandler>

    @set:Autowired
    lateinit var telegramBot: TelegramBot

    override fun initHandlers() {
        runBlocking {
            telegramBot.buildBehaviourWithLongPolling {
                attachmentHandlers.forEach {
                    with(it) {
                        handle()
                    }
                }
            }.join()
        }
    }
}