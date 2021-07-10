import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    id("com.diffplug.spotless") version "5.14.1"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "tf.sou.mc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17-R0.1-SNAPSHOT")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.0")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

configurations {
    testImplementation.get().extendsFrom(compileOnly.get())
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
        ktlint().userData(
            mapOf(
                "disabled_rules" to "no-wildcard-imports",
                "max_line_length" to "100"
            )
        )
        licenseHeaderFile("spotless.licence.kt")
        endWithNewline()
        trimTrailingWhitespace()
    }
}
