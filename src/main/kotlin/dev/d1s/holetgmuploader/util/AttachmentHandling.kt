package dev.d1s.holetgmuploader.util

import dev.d1s.hole.client.core.HoleClient
import dev.d1s.hole.client.exception.HoleClientException
import dev.inmo.tgbotapi.bot.exceptions.CommonRequestException
import dev.inmo.tgbotapi.extensions.api.files.downloadFileStream
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MediaContent
import io.ktor.http.*
import io.ktor.utils.io.jvm.javaio.*

suspend fun BehaviourContext.handleMediaContent(
    message: CommonMessage<out MediaContent>,
    holeClient: HoleClient,
    filename: String? = null,
) {
    handleClientExceptions(message) {
        try {
            replyWithObject(
                message,
                holeClient.postObject {
                    content = bot.downloadFileStream(message.content.media.fileId).toInputStream()

                    val objectName = filename ?: waitFilename(message)

                    fileName = objectName

                    val objectGroup = waitGroup(objectName, message, holeClient)

                    try {
                        holeClient.getGroup(objectGroup)
                    } catch (e: HoleClientException) {
                        if (e.error?.status == HttpStatusCode.NotFound.value) {
                            holeClient.postGroup {
                                name = objectGroup
                            }
                        }
                    }

                    group = objectGroup
                },
                holeClient
            )
        } catch (e: HoleClientException) {
            throw if (e.cause is CommonRequestException) {
                IllegalStateException("File is too big.")
            } else {
                e
            }
        }
    }
}