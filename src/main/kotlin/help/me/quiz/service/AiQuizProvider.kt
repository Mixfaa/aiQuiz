package help.me.quiz.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import help.me.ai.service.AiProvider
import help.me.quiz.model.Quiz
import help.me.quiz.model.QuizEntity
import help.me.quiz.model.QuizSubject
import help.me.utils.exceptions.GenerationFailed
import org.springframework.stereotype.Component

private fun writeQuizGenerationPrompt(
    topic: String,
    complexity: String,
    questionsCount: Int,
    additionalInfo: String
): String {
    return """
    Generate help.me.quiz, for given topic, provide only json code

    Option have this structure:
        {
            caption // string
            answer // true if this is correct answer, otherwise false
        }

        Question have this structure:
        {
            caption // string
            options[] // array of options
        }

        Quiz must have this structure:
        {
            name // string
            questions[] // array of questions
        }

        help.me.quiz topic: $topic  
        complexity: $complexity  
        questions count: $questionsCount   

        $additionalInfo  
            """
}

@Component
class AiQuizProvider(private val objectMapper: ObjectMapper, private val aiProvider: AiProvider) : QuizProvider {

    override fun getQuiz(
        subject: QuizSubject,
        topic: String,
        complexity: String,
        questionsCount: Int,
        additionalInfo: String
    ): Result<Quiz> {
        try {
            val prompt = writeQuizGenerationPrompt(topic, complexity, questionsCount, additionalInfo)

            val completionRequest = ChatCompletionRequest.builder()
                .messages(listOf(ChatMessage("user", prompt)))
                .model("gpt-3.5-turbo-16k")
                .build()

            val responseText =
                aiProvider.service().createChatCompletion(completionRequest).choices.first().message.content

            val json = responseText.substring(responseText.indexOf('{'), responseText.lastIndexOf('}') + 1)

            val quizEntityData = objectMapper.readValue(json, QuizEntity::class.java)

            quizEntityData.subject = subject
            quizEntityData.topic = topic
            quizEntityData.additionalInfo = additionalInfo

            return Result.success(quizEntityData)
        } catch (ex: Exception) {
            return Result.failure(GenerationFailed(ex))
        }
    }
}