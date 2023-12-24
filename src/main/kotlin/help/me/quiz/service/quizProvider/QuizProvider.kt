package help.me.quiz.service.quizProvider

import help.me.quiz.model.Quiz
import help.me.quiz.model.QuizSubject

interface QuizProvider {
    fun getQuiz(
        subject: QuizSubject,
        topic: String,
        complexity: String,
        questionsCount: Int,
        additionalInfo: String
    ): Result<Quiz>
}