package dev.d1s.holetgmuploader.handler

import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext

interface AttachmentHandler {

    suspend fun BehaviourContext.handle()
}