package help.me.authentication.service

import help.me.authentication.model.Account
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface AccountRepository : ReactiveMongoRepository<Account, ObjectId> {
    fun findByUsername(username: String): Mono<Account>
    fun existsByUsername(username: String): Mono<Boolean>
}