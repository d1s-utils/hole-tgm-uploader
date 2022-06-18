package dev.d1s.holetgmuploader.util

import dev.d1s.hole.client.core.HoleClient
import dev.d1s.hole.client.entity.storageObject.StorageObject
import dev.inmo.tgbotapi.extensions.api.send.reply
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

suspend fun BehaviourContext.waitGroup(filename: String, message: Message, holeClient: HoleClient): String {
    val groupReplyMarkup = ReplyKeyboardMarkup(
        matrix {
            val groups = runBlocking {
                holeClient.getAvailableGroups()
            }

            groups.chunked(3).forEach {
                add(
                    it.map { group ->
                        SimpleKeyboardButton(group)
                    }
                )
            }
        },
        resizeKeyboard = true
    )

    return waitText(
        SendTextMessage(
            message.chat.id,
            listOf(
                regular("Send me the group to upload to ("),
                code(filename),
                regular(")")
            ),
            replyMarkup = groupReplyMarkup
        )
    ).first().text
}

suspend fun BehaviourContext.waitFilename(message: Message) = waitText(
    SendTextMessage(
        message.chat.id,
        "Send me the filename to use."
    )
).first().text

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