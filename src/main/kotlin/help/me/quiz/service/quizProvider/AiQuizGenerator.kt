package help.me.quiz.service.quizProvider

import com.fasterxml.jackson.databind.ObjectMapper
import com.theokanning.openai.client.OpenAiApi
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.service.OpenAiService
import help.me.utils.exceptions.GenerationFailed
import help.me.quiz.model.Quiz
import help.me.quiz.model.QuizEntity
import help.me.quiz.model.QuizSubject
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.Duration

private fun writePromptToGenerateQuiz(
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
class AiQuizGenerator(private val objectMapper: ObjectMapper) : QuizProvider {
    @Value("\${aiprovider.apikey}")
    private final lateinit var aiApikey: String

    @Value("\${aiprovider.baseurl}")
    private final lateinit var aiBaseurl: String
    private final lateinit var apiService: OpenAiService

    @PostConstruct
    private fun postConstruct() {
        val objectMapper = OpenAiService.defaultObjectMapper()
        val httpClient = OpenAiService.defaultClient(aiApikey, Duration.ofSeconds(1000))

        val retrofit = Retrofit.Builder().client(httpClient).baseUrl(aiBaseurl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(objectMapper)).build()

        val openAiApi = retrofit.create(OpenAiApi::class.java)

        apiService = OpenAiService(openAiApi)
    }

    override fun getQuiz(
        subject: QuizSubject,
        topic: String,
        complexity: String,
        questionsCount: Int,
        additionalInfo: String
    ): Result<Quiz> {
        try {
            val prompt = writePromptToGenerateQuiz(topic, complexity, questionsCount, additionalInfo)

            val completionRequest = ChatCompletionRequest.builder()
                .messages(listOf(ChatMessage("user", prompt)))
                .model("gpt-3.5-turbo-16k")
                .build()

            val responseText = apiService.createChatCompletion(completionRequest).choices.first().message.content

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

    companion object {
        fun forQuiz(topic: String, complexity: String, questionsCount: Int, additionalInfo: String): String {
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
    }
}