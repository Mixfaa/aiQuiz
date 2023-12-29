package help.me.quiz.service

import help.me.quiz.model.Quiz
import help.me.quiz.model.QuizSubject
import reactor.core.publisher.Flux

interface QuizProvider {
    fun quizzes(
        subject: QuizSubject,
        topic: String,
        complexity: String,
        questionsCount: Int,
        additionalInfo: String
    ): Flux<Quiz>
}