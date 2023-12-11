package help.me.aiQuiz.controllers

import help.me.aiQuiz.models.Quiz
import help.me.aiQuiz.models.QuizSubject
import help.me.aiQuiz.services.QuizService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class QuizController(
    val quizService: QuizService,
) {
    @GetMapping("/quizzes")
    fun quizzes(): List<Quiz> {
        return quizService.listQuizzes()
    }

    @DeleteMapping("/quiz")
    fun deleteQuiz(id: Long) {
        quizService.deleteQuiz(id)
    }

    @PutMapping("/quiz")
    fun saveQuiz(quiz: Quiz): Long {
        return quizService.saveQuiz(quiz)
    }

    @PostMapping("/generateQuiz")
    fun generateQuiz(
        quizSubject: QuizSubject,
        topic: String,
        complexity: String,
        questionsCount: Int,
        additionalInfo: Optional<String>
    ): Quiz {
        return quizService.tryGenerateQuiz(quizSubject, topic, complexity, questionsCount, additionalInfo.orElse(""))
    }
}