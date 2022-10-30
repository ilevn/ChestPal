import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    id("com.diffplug.spotless") version "5.14.1"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "tf.sou.mc"
version = "1.3-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testImplementation("org.assertj:assertj-core:3.23.1")
    implementation(kotlin("stdlib-jdk8"))
}

configurations {
    testImplementation.get().extendsFrom(compileOnly.get())
}

tasks.test {
    useJUnitPlatform()
}

java {
  toolchain {
      languageVersion.set(JavaLanguageVersion.of(17))
  }
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
