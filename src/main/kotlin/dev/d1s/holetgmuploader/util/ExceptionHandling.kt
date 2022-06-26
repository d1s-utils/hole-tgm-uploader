package dev.d1s.holetgmuploader.util

import dev.d1s.hole.client.exception.HoleClientException
import dev.d1s.holetgmuploader.exception.UploadAbortedException
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.types.message.abstracts.Message

suspend inline fun BehaviourContext.handleClientExceptions(message: Message, block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        if (e.cause !is UploadAbortedException) {
            reply(
                message, (e as? HoleClientException)?.error?.error ?: e.message ?: "Unknown error".also {
                    e.printStackTrace()
                }
            )
        }
    }
}