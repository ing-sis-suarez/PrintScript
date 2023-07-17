package config

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

data class FormatterConfig(
    @JsonProperty("spacesBetweenTokens")
    val spacesBetweenTokens: Int = 1,
    @JsonProperty("indentSize")
    val indentSize: Int = 4,
    @JsonProperty("maxLineLength")
    val maxLineLength: Int = 80
) {
    companion object {
        private val objectMapper = ObjectMapper()
        fun loadFromJsonFile(file: File): FormatterConfig {
            return if (file.exists()) {
                objectMapper.readValue(file, FormatterConfig::class.java)
            } else {
                FormatterConfig()
            }
        }
    }
}
