package help.me.quiz.controller

import help.me.authentication.model.Account
import help.me.quiz.model.Quiz
import help.me.quiz.model.QuizEntity
import help.me.quiz.model.QuizSubject
import help.me.quiz.service.QuizService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal
import java.util.*

private val invalidPrincipalThrowable = Throwable("Principal is not instance of account")

@RestController
@RequestMapping("/api/quiz")
class QuizController(
    val quizService: QuizService,
) {
    @GetMapping("/search")
    fun searchForQuizzes(query: String, page: Int, limit: Int, principal: Principal): Flux<QuizEntity> {
        return quizService.searchForQuizzes(query, PageRequest.of(page, limit))
    }

    @GetMapping("/search_by_subj")
    fun getQuizzesBySubject(subject: QuizSubject, page: Int, limit: Int): Flux<QuizEntity> {
        return quizService.searchForQuizzesBySubject(subject, PageRequest.of(page, limit))
    }

    @GetMapping("/list_all")
    fun listQuizzes(page: Int, limit: Int): Flux<QuizEntity> {
        return quizService.listQuizzes(PageRequest.of(page, limit))
    }

    @PostMapping("/delete_cached")
    fun deleteCachedQuiz(id: String, principal: Principal): Mono<Boolean> {
        if (principal !is Account) return Mono.error(invalidPrincipalThrowable)
        return quizService.deleteCachedQuiz(id, principal)
    }

    @PostMapping("/delete")
    fun deleteQuiz(id: String, principal: Principal): Mono<Boolean> {
        if (principal !is Account) return Mono.error(invalidPrincipalThrowable)
        return quizService.deleteQuiz(id, principal)
    }

    @PostMapping("/save_cached")
    fun saveCachedQuiz(id: String, principal: Principal): Mono<QuizEntity> {
        if (principal !is Account) return Mono.error(invalidPrincipalThrowable)
        return quizService.saveCachedQuiz(id, principal)
    }

    @PostMapping("/save_custom")
    fun saveCustomQuiz(quiz: Quiz, principal: Principal): Mono<QuizEntity> {
        if (principal !is Account) return Mono.error(invalidPrincipalThrowable)
        return quizService.saveCustomQuiz(quiz, principal)
    }

    @PostMapping("/generate")
    fun generateQuiz(
        quizSubject: QuizSubject,
        topic: String,
        complexity: String,
        questionsCount: Int,
        principal: Principal,
        additionalInfo: Optional<String>
    ): Flux<Quiz> {
        if (principal !is Account) return Flux.error(invalidPrincipalThrowable)
        return quizService.tryGenerateQuiz(
            quizSubject,
            topic,
            complexity,
            questionsCount,
            additionalInfo.orElse(""),
            principal
        )
    }
}