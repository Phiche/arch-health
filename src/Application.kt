package phicher.course

import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.feature
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import liquibase.Contexts
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.jetbrains.exposed.sql.Database
import phicher.course.dto.User
import phicher.course.service.UserRepository
import java.lang.Exception
import java.time.Duration


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val userRepository = UserRepository()
    initDB()
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(MicrometerMetrics) {
        val promRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        registry = promRegistry
        distributionStatisticConfig = DistributionStatisticConfig.Builder()
            .percentilesHistogram(true)
            .build()

        routing {
            get("/prometheus") {
                call.respondText {
                    promRegistry.scrape()
                }
            }
        }
    }

    routing {
        get("/health") {
            call.respond(mapOf("status" to "OK"))
        }

        get("/") {
            call.respond(HttpStatusCode.OK, "Alive!!")
        }

        post("/user") {
            val user = call.receive<User>()
            val createdUser = userRepository.createUser(user)

            call.respond(HttpStatusCode.Created, createdUser)
        }

        delete("/user/{id}") {
            val id = call.parameters["id"]
                ?: throw Exception("Id must not be null!")
            val delRowsCount = userRepository.deleteById(id)

            call.respond(HttpStatusCode.OK, delRowsCount)
        }

        get("/user/{id}") {
            val id = call.parameters["id"]
                ?: throw Exception("Id must not be null!")
            val user = userRepository.findById(id)

            call.respond(HttpStatusCode.OK, user)
        }

        put("/user") {
            val user = call.receive<User>()
            val updatedRowsCount = userRepository.updateUser(user)

            call.respond(HttpStatusCode.OK, updatedRowsCount)
        }
    }

}

private fun initDB() {
    val config = HikariConfig()
    config.jdbcUrl = System.getenv("JDBC_URL")
    config.username = System.getenv("USERNAME")
    config.password = System.getenv("PASSWORD")

    val ds = HikariDataSource(config)
    val connection = ds.connection
    val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(connection))
    val liquibase = Liquibase("/db/changelog.xml", ClassLoaderResourceAccessor(), database)
    liquibase.update(Contexts(), LabelExpression())
    Database.connect(ds)
}

