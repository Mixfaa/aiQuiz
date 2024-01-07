package help.me.quiz.service

import help.me.authentication.model.Account
import help.me.quiz.model.QuizEntity
import help.me.quiz.model.QuizSubject
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface QuizRepository : ReactiveMongoRepository<QuizEntity, String> {
    @Query("{ \$text :  {\$search: \"?1\"}}")
    fun findAllByText(query: String, pageable: Pageable): Flux<QuizEntity>
    fun findAllBySubject(subject: QuizSubject, pageable: Pageable): Flux<QuizEntity>

    fun findByIdAndCreator(id: String, creator: Account): Mono<QuizEntity>
    fun deleteByIdAndCreator(id: String, creator: Account): Mono<Boolean>
    fun findAllBy(pageable: Pageable): Flux<QuizEntity>
}