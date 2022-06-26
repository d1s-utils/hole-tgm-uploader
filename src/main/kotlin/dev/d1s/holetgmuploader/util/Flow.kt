package dev.d1s.holetgmuploader.util

import dev.d1s.hole.client.core.HoleClient
import dev.d1s.hole.client.entity.storageObject.StorageObject
import dev.d1s.holetgmuploader.exception.UploadAbortedException
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardMarkup
import dev.inmo.tgbotapi.types.buttons.SimpleKeyboardButton
import dev.inmo.tgbotapi.types.message.abstracts.Message
import dev.inmo.tgbotapi.types.message.textsources.code
import dev.inmo.tgbotapi.types.message.textsources.regular
import dev.inmo.tgbotapi.utils.matrix
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private const val ABORTION_COMMAND = "abort"

suspend fun BehaviourContext.waitGroup(filename: String, message: Message, holeClient: HoleClient): String {
    val groupReplyMarkup = ReplyKeyboardMarkup(
        matrix {
            val groups = runBlocking {
                holeClient.getAllGroups().map {
                    it.name
                }
            }

            groups.chunked(3).forEach {
                add(
                    it.map { group ->
                        SimpleKeyboardButton(group)
                    }
                )
            }

            add(listOf(SimpleKeyboardButton(ABORTION_COMMAND)))
        },
        resizeKeyboard = true
    )

    val text = waitText(
        SendTextMessage(
            message.chat.id,
            listOf(
                regular("Send me the group to upload to ("),
                code(filename),
                regular("). Or reply with "),
                code(ABORTION_COMMAND),
                regular(" to abort.")
            ),
            replyMarkup = groupReplyMarkup
        )
    ).first().text

    if (text == ABORTION_COMMAND) {
        abort(message)
    }

    return text
}

suspend fun BehaviourContext.waitFilename(message: Message): String {
    val text = waitText(
        SendTextMessage(
            message.chat.id,
            listOf(
                regular("Send me the filename to use. Or reply with "),
                code(ABORTION_COMMAND),
                regular(" to abort.")
            ),
            replyMarkup = ReplyKeyboardMarkup(
                matrix {
                    add(listOf(SimpleKeyboardButton(ABORTION_COMMAND)))
                },
                resizeKeyboard = true
            )

        )
    ).first().text

    if (text == ABORTION_COMMAND) {
        abort(message)
    }

    return text
}

suspend fun BehaviourContext.replyWithObject(message: Message, obj: StorageObject, holeClient: HoleClient) {
    reply(
        message,
        listOf(
            regular("Successfully uploaded "),
            code(obj.name),
            regular(" with ID "),
            code(obj.id),
            regular(" on "),
            code(holeClient.configuration.baseUrl.toString())
        )
    )
}

private suspend fun BehaviourContext.abort(message: Message) {
    sendMessage(message.chat, "Aborted.")
    throw UploadAbortedException()
}