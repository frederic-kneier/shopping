package de.solugo.shopping.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import de.solugo.shopping.ApplicationProperties
import de.solugo.shopping.model.ShoppingList
import de.solugo.shopping.model.ShoppingEntry
import kotlinx.coroutines.runBlocking
import org.bson.UuidRepresentation
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.keyProjection
import org.litote.kmongo.reactivestreams.KMongo
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableAutoConfiguration(exclude = [MongoAutoConfiguration::class])
class MongoConfiguration {

    @Bean
    fun mongoSettings(
        properties: ApplicationProperties,
    ): MongoClientSettings = MongoClientSettings.builder().run {
        uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
        applyConnectionString(ConnectionString(properties.mongo.connectionUri))
        build()
    }

    @Bean(destroyMethod = "close")
    fun mongoClient(
        settings: MongoClientSettings,
    ): CoroutineClient = KMongo.createClient(settings).coroutine

    @Bean
    fun mongoDatabase(
        properties: ApplicationProperties,
        client: CoroutineClient,
    ): CoroutineDatabase = runBlocking {
        client.getDatabase(properties.mongo.database)
    }

    @Bean
    fun mongoShoppingLists(
        database: CoroutineDatabase,
    ) = runBlocking {
        database.getCollection<ShoppingList>("lists").apply {
            ensureIndex(ShoppingList::principalRoles.keyProjection("$**"))
        }
    }

    @Bean
    fun mongoShoppingEntries(
        database: CoroutineDatabase,
    ) = runBlocking {
        database.getCollection<ShoppingEntry>("entries").apply {
            ensureIndex(ShoppingEntry::title)
        }
    }

}