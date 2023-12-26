package help.me.quiz.controller

import help.me.quiz.model.Quiz
import help.me.quiz.model.QuizSubject
import help.me.quiz.service.QuizService
import help.me.utils.exceptions.foldToResponseEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/quiz")
class QuizController(
    val quizService: QuizService,
) {
    @GetMapping("/search")
    fun searchForQuizzes(query: String, page: Int, limit: Int): ResponseEntity<*> {
        return ResponseEntity.ok(quizService.searchForQuizzes(query, PageRequest.of(page, limit)).toList())
    }

    @GetMapping("/search_by_subj")
    fun getQuizzesBySubject(subject: QuizSubject, page: Int, limit: Int): ResponseEntity<*> {
        return ResponseEntity.ok(quizService.searchForQuizzesBySubject(subject, PageRequest.of(page, limit)).toList())
    }

    @GetMapping("/list_all")
    fun listQuizzes(page: Int, limit: Int): ResponseEntity<*> {
        return ResponseEntity.ok(quizService.listQuizzes(PageRequest.of(page, limit)))
    }

    @PostMapping("/delete_cached")
    fun deleteCachedQuiz(id: Long): ResponseEntity<*> {
        quizService.deleteCachedQuiz(id)
        return ResponseEntity.ok().build<Unit>()
    }

    @PostMapping("/delete")
    fun deleteQuiz(id: String): ResponseEntity<*> {
        quizService.deleteQuiz(id)
        return ResponseEntity.ok().build<Unit>()
    }

    @PostMapping("/save_cached")
    fun saveCachedQuiz(id: Long): ResponseEntity<*> {
        return quizService.saveCachedQuiz(id).foldToResponseEntity()
    }

    @PostMapping("/save_custom")
    fun saveCustomQuiz(quiz: Quiz): ResponseEntity<*> {
        return quizService.saveCustomQuiz(quiz).foldToResponseEntity()
    }

    @PostMapping("/generate")
    fun generateQuiz(
        quizSubject: QuizSubject,
        topic: String,
        complexity: String,
        questionsCount: Int,
        additionalInfo: Optional<String>
    ): ResponseEntity<*> {
        return quizService.tryGenerateQuiz(quizSubject, topic, complexity, questionsCount, additionalInfo.orElse(""))
            .foldToResponseEntity()
    }
}