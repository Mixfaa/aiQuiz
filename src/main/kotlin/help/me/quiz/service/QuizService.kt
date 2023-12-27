package help.me.quiz.service

import help.me.authentication.model.Account
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
    fun saveCachedQuiz(cachedId: Long, creator: Account): Result<QuizEntity> {
        val cachedQuiz = quizCachedRepository.findByIdOrNull(cachedId)
            ?: return Result.failure(Throwable("Quiz not found in cache)"))

        return Result.success(quizRepository.save(QuizEntity(cachedQuiz, creator)))
    }

    fun saveCustomQuiz(quiz: Quiz, creator: Account): Result<QuizEntity> {
        return Result.success(quizRepository.save(QuizEntity(quiz, creator)))
    }

    fun deleteQuiz(id: String, account: Account): Result<Unit> {
        val quiz = quizRepository.findByIdAndCreator(ObjectId(id), account)
            ?: return Result.failure(Throwable("Quiz not found or you are not it`s creator"))
        quizRepository.delete(quiz)
        return Result.success(Unit)
    }

    fun deleteCachedQuiz(id: Long, account: Account): Result<Unit> {
        val cachedQuiz = quizCachedRepository.findByIdAndCreator(id, account)
            ?: return Result.failure(Throwable("Quiz not found or you are not it`s creator"))
        quizCachedRepository.delete(cachedQuiz)
        return Result.success(Unit)
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
        subject: QuizSubject,
        topic: String,
        complexity: String,
        questionsCount: Int,
        additionalInfo: String,
        creator: Account
    ): Result<Quiz> {
        val quizResult = quizProvider.getQuiz(subject, topic, complexity, questionsCount, additionalInfo)
        if (quizResult.isFailure) return quizResult

        val quiz = quizResult.getOrNull()!!

        quizCachedRepository.save(QuizCached(quiz, creator))

        return quizResult
    }
}