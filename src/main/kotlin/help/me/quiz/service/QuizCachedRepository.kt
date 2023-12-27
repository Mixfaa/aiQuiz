package help.me.quiz.service

import help.me.authentication.model.Account
import help.me.quiz.model.QuizCached
import org.springframework.data.repository.CrudRepository

interface QuizCachedRepository : CrudRepository<QuizCached, Long> {
    fun findByIdAndCreator(id: Long, creator: Account): QuizCached?
}