package help.me.quiz.service

import help.me.quiz.model.QuizCached
import org.springframework.data.repository.CrudRepository

interface QuizCachedRepository : CrudRepository<QuizCached, Long>