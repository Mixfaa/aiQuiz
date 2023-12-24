package help.me.quiz.service

import help.me.quiz.model.QuizEntity
import help.me.quiz.model.QuizSubject
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface QuizRepository : MongoRepository<QuizEntity, ObjectId> {
    @Query("{ \$text :  {\$search: \"?1\"}}")
    fun findAllByText(query: String, pageable: Pageable): Page<QuizEntity>

    fun findAllBySubject(subject: QuizSubject, pageable: Pageable): Page<QuizEntity>
}