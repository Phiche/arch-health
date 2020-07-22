package phicher.course.service

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import phicher.course.dto.User
import phicher.course.dto.Users
import java.util.*
import java.util.UUID.fromString

class UserRepository {
    fun findById(id: String): User {
        return transaction {
            addLogger(StdOutSqlLogger)
            Users.select { Users.id.eq(fromString(id)) }.single().toDto()
        }
    }

    fun createUser(user: User): User {
        return transaction {
            Users.insert {
                it[id] = UUID.randomUUID()
                it[username] = user.username
                it[mail] = user.mail
                it[firstName] = user.firstName
                it[lastName] = user.lastName
            }.resultedValues!![0].toDto()
        }

    }

    fun updateUser(user: User): Int {
        return transaction {
            Users.update( { Users.id eq fromString(user.id) } ) {
                it[username] = user.username
                it[mail] = user.mail
                it[firstName] = user.firstName
                it[lastName] = user.lastName
            }
        }

    }

    fun deleteById(id: String): Int {
        return transaction {
            Users.deleteWhere { Users.id eq fromString(id) }
        }
    }
}

private fun ResultRow.toDto(): User {
    return User(
        id = get(Users.id).toString(),
        username = get(Users.username).toString(),
        firstName = getOrNull(Users.firstName).toString(),
        lastName = getOrNull(Users.lastName).toString(),
        mail = get(Users.mail).toString()

    )
}
