package help.me.quiz.service

import jakarta.annotation.PostConstruct
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.stereotype.Component


@Component
class CachedQuizLoader(
    private val factory: ReactiveRedisConnectionFactory,
) {
    @PostConstruct
    fun loadData() {
        factory.reactiveConnection.serverCommands().flushAll()
    }

}