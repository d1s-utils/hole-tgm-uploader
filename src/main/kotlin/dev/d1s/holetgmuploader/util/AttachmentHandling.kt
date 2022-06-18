package dev.d1s.holetgmuploader.util

import dev.d1s.hole.client.core.HoleClient
import dev.inmo.tgbotapi.extensions.api.files.downloadFileStream
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MediaContent
import io.ktor.utils.io.jvm.javaio.*

suspend fun BehaviourContext.handleMediaContent(
    message: CommonMessage<out MediaContent>,
    holeClient: HoleClient,
    filename: String? = null,
) {
    handleClientExceptions(message) {
        replyWithObject(
            message,
            holeClient.postObject {
                content = bot.downloadFileStream(message.content.media.fileId).toInputStream()

                val name = filename ?: waitFilename(message)

                fileName = name

                group = waitGroup(name, message, holeClient)
            },
            holeClient
        )
    }
}