package help.me.ai.service

import com.theokanning.openai.client.OpenAiApi
import com.theokanning.openai.service.OpenAiService
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.Duration

@Component
class FreeAiProvider(
    @Value("\${aiprovider.apikey}") private val aiApiKey: String,
    @Value("\${aiprovider.baseurl}") private val aiBaseurl: String,
) : AiProvider {

    private val apiService: OpenAiService by lazy {
        val objectMapper = OpenAiService.defaultObjectMapper()
        val httpClient = OpenAiService.defaultClient(aiApiKey, Duration.ofSeconds(1000))

        val retrofit = Retrofit.Builder().client(httpClient).baseUrl(aiBaseurl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(objectMapper)).build()

        val openAiApi = retrofit.create(OpenAiApi::class.java)

        OpenAiService(openAiApi)
    }

    override fun service(): OpenAiService {
        return apiService
    }
}