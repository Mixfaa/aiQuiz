package help.me.ai.service

import com.theokanning.openai.service.OpenAiService

interface AiProvider {
    fun service(): OpenAiService
}