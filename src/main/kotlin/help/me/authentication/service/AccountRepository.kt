package help.me.authentication.service

import help.me.authentication.model.Account
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : MongoRepository<Account, ObjectId> {
    fun findByUsername(username: String): Account?
    fun existsByUsername(username:String) : Boolean
}