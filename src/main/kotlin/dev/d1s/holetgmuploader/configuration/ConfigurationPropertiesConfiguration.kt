package dev.d1s.holetgmuploader.configuration

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan("dev.d1s.holetgmuploader.properties")
class ConfigurationPropertiesConfiguration