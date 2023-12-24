package help.me.quiz.service

import help.me.quiz.model.Quiz
import help.me.quiz.model.QuizCached
import help.me.quiz.model.QuizEntity
import help.me.quiz.model.QuizSubject
import help.me.quiz.service.quizProvider.QuizProvider
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class QuizService(
    private val quizProvider: QuizProvider,
    private val quizCachedRepository: QuizCachedRepository,
    private val quizRepository: QuizRepository
) {
    fun saveCachedQuiz(cachedId: Long): Result<QuizEntity> {
        val cachedQuiz = quizCachedRepository.findByIdOrNull(cachedId)
            ?: return Result.failure(Throwable("Quiz not found in cache)"))

        return Result.success(quizRepository.save(QuizEntity(cachedQuiz)))
    }

    fun saveCustomQuiz(quiz: Quiz): Result<QuizEntity> {
        return Result.success(quizRepository.save(QuizEntity(quiz)))
    }

    fun deleteQuiz(id: String) {
        quizRepository.deleteById(ObjectId(id))
    }

    fun deleteCachedQuiz(id: Long) {
        return quizCachedRepository.deleteById(id)
    }

    fun searchForQuizzes(query: String, pageable: Pageable): Page<QuizEntity> {
        return quizRepository.findAllByText(query, pageable)
    }

    fun searchForQuizzesBySubject(subject: QuizSubject, pageable: Pageable): Page<QuizEntity> {
        return quizRepository.findAllBySubject(subject, pageable)
    }

    fun listQuizzes(pageable: Pageable): Page<QuizEntity> {
        return quizRepository.findAll(pageable)
    }

    fun tryGenerateQuiz(
        subject: QuizSubject, topic: String, complexity: String, questionsCount: Int, additionalInfo: String
    ): Result<Quiz> {
        val quizResult = quizProvider.getQuiz(subject, topic, complexity, questionsCount, additionalInfo)
        if (quizResult.isFailure) return quizResult

        val quiz = quizResult.getOrNull()!!

        quizCachedRepository.save(QuizCached(quiz))

        return quizResult
    }
}