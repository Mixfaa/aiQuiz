package help.me.aiQuiz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
class AiQuizApplication

fun main(args: Array<String>) {
	runApplication<AiQuizApplication>(*args)
}
