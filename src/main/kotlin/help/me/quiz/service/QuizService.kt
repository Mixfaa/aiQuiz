package help.me.quiz.service

import help.me.authentication.model.Account
import help.me.quiz.model.Quiz
import help.me.quiz.model.QuizCached
import help.me.quiz.model.QuizEntity
import help.me.quiz.model.QuizSubject
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.*

@Service
class QuizService(
    private val quizProvider: QuizProvider,
    private val quizOps: ReactiveRedisOperations<String, QuizCached>,
    private val quizRepository: QuizRepository
) {
    fun saveCachedQuiz(cachedId: String, creator: Account): Mono<QuizEntity> {
        return quizOps.opsForValue().get(cachedId)
            .flatMap {
                quizRepository.save(QuizEntity(it, creator))
            }
    }

    fun saveCustomQuiz(quiz: Quiz, creator: Account): Mono<QuizEntity> {
        return quizRepository.save(QuizEntity(quiz, creator))
    }

    fun deleteQuiz(id: String, account: Account): Mono<Boolean> {
        return quizRepository.deleteByIdAndCreator(id, account)
    }

    fun deleteCachedQuiz(cachedId: String, account: Account): Mono<Boolean> {
        return quizOps.opsForValue().get(cachedId)
            .filter { it.creator == account }
            .hasElement()
            .flatMap { hasElement ->
                if (hasElement)
                    return@flatMap quizOps.delete(cachedId).thenReturn(true)
                else
                    return@flatMap Mono.just(false)
            }
    }

    fun searchForQuizzes(query: String, pageable: Pageable): Flux<QuizEntity> {
        return quizRepository.findAllByText(query, pageable)
    }

    fun searchForQuizzesBySubject(subject: QuizSubject, pageable: Pageable): Flux<QuizEntity> {
        return quizRepository.findAllBySubject(subject, pageable)
    }

    fun listQuizzes(pageable: Pageable): Flux<QuizEntity> {
        return quizRepository.findAllBy(pageable)
    }

    fun tryGenerateQuiz(
        subject: QuizSubject,
        topic: String,
        complexity: String,
        questionsCount: Int,
        additionalInfo: String,
        creator: Account
    ): Flux<Quiz> {
        return quizProvider.quizzes(subject, topic, complexity, questionsCount, additionalInfo)
            .publishOn(Schedulers.boundedElastic())
            .doOnNext {
                quizOps.opsForValue().set(UUID.randomUUID().toString(), QuizCached(it, creator)).subscribe()
            }
    }
}