plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.9"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "io.github.isidoresong"
version = "0.0.1-SNAPSHOT"
description = "legacy-refactor-poc-context"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-validation")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test"){
        exclude(group = "org.mockito", module = "mockito-core")
        exclude(group = "org.mockito", module = "mockito-junit-jupiter")
        exclude(group = "org.mockito", module = "mockito-inline")
    }

    testImplementation("io.kotest:kotest-runner-junit5-jvm:6.0.7")
    testImplementation("io.kotest:kotest-assertions-core-jvm:6.0.7")
    testImplementation("io.kotest:kotest-extensions-spring-jvm:6.0.7")

    testImplementation("io.mockk:mockk:1.14.7")

    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
