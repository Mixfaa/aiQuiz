package help.me.quiz.service

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