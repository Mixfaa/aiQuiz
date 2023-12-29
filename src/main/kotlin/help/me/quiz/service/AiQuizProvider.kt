package help.me.quiz.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import help.me.ai.service.AiProvider
import help.me.quiz.model.Quiz
import help.me.quiz.model.QuizSubject
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux

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

    override fun quizzes(
        subject: QuizSubject,
        topic: String,
        complexity: String,
        questionsCount: Int,
        additionalInfo: String
    ): Flux<Quiz> {
        val prompt = writeQuizGenerationPrompt(topic, complexity, questionsCount, additionalInfo)

        val completionRequest = ChatCompletionRequest.builder()
            .messages(listOf(ChatMessage("user", prompt)))
            .model("gpt-3.5-turbo-16k")
            .build()

        val completion = aiProvider.service().createChatCompletion(completionRequest)

        return completion.choices.toFlux()
            .map {
                try {

                    val responseText = it.message.content
                    val json = responseText.substring(responseText.indexOf('{'), responseText.lastIndexOf('}') + 1)

                    val quiz = objectMapper.readValue(json, Quiz::class.java)

                    quiz.subject = subject
                    quiz.topic = topic
                    quiz.additionalInfo = additionalInfo

                    return@map quiz
                } catch (ex: Exception) {
                    return@map null
                }
            }
            .filter { it != null }
            .cast(Quiz::class.java)
    }
}