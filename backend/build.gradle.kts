import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.21"
    id("org.springframework.boot") version "3.2.0"
}

group = "de.solugo.tasks"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.2.0"))
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.7.3"))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.litote.kmongo:kmongo-coroutine:4.11.0")
    implementation("org.valiktor:valiktor-spring-boot-starter:0.12.0")

    testImplementation(platform("org.testcontainers:testcontainers-bom:1.19.3"))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation(platform("io.ktor:ktor-bom:2.3.6"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.kotest:kotest-assertions-json:5.8.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("org.testcontainers:mongodb")
    testImplementation("io.ktor:ktor-client-java")
    testImplementation("io.ktor:ktor-client-content-negotiation")
    testImplementation("io.ktor:ktor-serialization-jackson")
}

kotlin {
    jvmToolchain(21)
}

springBoot {
    buildInfo()
}

tasks.withType<BootJar> {
    archiveClassifier.set("application")
    layered {
        enabled.set(true)
    }
}

tasks.withType<KotlinJvmCompile> {
    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_1_9)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

