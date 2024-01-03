package help.me

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import help.me.quiz.model.QuizCached
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.reactive.config.EnableWebFlux


@EnableMongoRepositories
@EnableRedisRepositories
@EnableWebFlux
@EnableWebFluxSecurity
@Configuration
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class AiQuizApplication {
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerKotlinModule()
    }

    @Bean
    fun bcryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun redisOperations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, QuizCached> {
        val serializer = Jackson2JsonRedisSerializer(QuizCached::class.java)
        val builder = RedisSerializationContext.newSerializationContext<String, QuizCached>(StringRedisSerializer())
        val context = builder.value(serializer).build()

        return ReactiveRedisTemplate(factory, context)
    }
}

fun main(args: Array<String>) {
    runApplication<AiQuizApplication>(*args)
}
