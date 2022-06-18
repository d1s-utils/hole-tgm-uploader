package dev.d1s.holetgmuploader.initializer

import dev.d1s.holetgmuploader.service.AttachmentHandlingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class AttachmentHandlerInitializer : ApplicationRunner {

    @set:Autowired
    lateinit var attachmentHandlingService: AttachmentHandlingService

    override fun run(args: ApplicationArguments) {
        attachmentHandlingService.initHandlers()
    }
}