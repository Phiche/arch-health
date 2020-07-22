package phicher.course.dto

import org.jetbrains.exposed.sql.Table

object Users: Table() {
    val id = uuid("id").primaryKey()
    val firstName = text("firstname").nullable()
    val mail = text("mail")
    val lastName = text("lastname").nullable()
    val username = text("username")
}