package help.me.aiQuiz.services

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import help.me.aiQuiz.models.Quiz
import help.me.aiQuiz.models.QuizSubject
import help.me.aiQuiz.utils.PromptWriter
import org.springframework.stereotype.Service

@Service
class QuizService(
    val aiService: AiApiService,
    val quizRepository: QuizRepository
) {
    private final val objectMapper = ObjectMapper().enable(JsonParser.Feature.IGNORE_UNDEFINED)

    fun saveQuiz(quiz: Quiz): Long {
        val foundQuiz = quizRepository.findById(quiz.id)

        if (foundQuiz.isPresent)
            return foundQuiz.get().id

        return quizRepository.save(quiz).id
    }

    fun deleteQuiz(id: Long) {
        quizRepository.deleteById(id)
    }

    fun listQuizzes(): List<Quiz> {
        return quizRepository.findAll()
    }

    fun tryGenerateQuiz(
        subject: QuizSubject,
        topic: String,
        complexity: String,
        questionsCount: Int,
        additionalInfo: String
    ): Quiz {

        val prompt = PromptWriter.forQuiz(topic, complexity, questionsCount, additionalInfo)

        val completionRequest = ChatCompletionRequest.builder()
            .messages(listOf(ChatMessage("user", prompt)))
            .model("gpt-3.5-turbo")
            .build()

        //val responseText = this.javaClass.getResource("/testResponse.txt").readText()
        val responseText = aiService.apiService.createChatCompletion(completionRequest).choices.first().message.content

        println(responseText)

        val json = responseText.substring(responseText.indexOf('{'), responseText.lastIndexOf('}') + 1)

        val quiz = objectMapper.readValue(json, Quiz::class.java)

        quiz.subject = subject
        quiz.topic = topic

        quizRepository.save(quiz)

        return quiz
    }
}