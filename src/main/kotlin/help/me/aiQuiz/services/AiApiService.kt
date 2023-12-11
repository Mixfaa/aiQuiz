package help.me.aiQuiz.services

import com.theokanning.openai.client.OpenAiApi
import com.theokanning.openai.service.OpenAiService
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.Duration


@Service
class AiApiService(val quizRepository: QuizRepository) {
    @Value("\${aiprovider.apikey}")
    private final lateinit var aiApikey: String

    @Value("\${aiprovider.baseurl}")
    private final lateinit var aiBaseurl: String
    final lateinit var apiService: OpenAiService
        private set


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
}