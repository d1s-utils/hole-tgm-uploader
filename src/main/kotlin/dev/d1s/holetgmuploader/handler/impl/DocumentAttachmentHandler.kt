package dev.d1s.holetgmuploader.handler.impl

import dev.d1s.hole.client.core.HoleClient
import dev.d1s.holetgmuploader.handler.AttachmentHandler
import dev.d1s.holetgmuploader.util.handleMediaContent
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DocumentAttachmentHandler : AttachmentHandler {

    @set:Autowired
    lateinit var holeClient: HoleClient

    override suspend fun BehaviourContext.handle() {
        onDocument {
            handleMediaContent(it, holeClient, it.content.media.fileName!!)
        }
    }
}