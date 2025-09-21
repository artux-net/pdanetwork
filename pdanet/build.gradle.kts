val detektVersion = "1.23.6"

plugins {
    java
    id("org.springframework.boot") version "3.1.4"
    id("org.hibernate.orm") version "6.6.4.Final"
    id("org.jetbrains.kotlin.jvm") version "1.9.24"
    id("io.freefair.lombok") version "8.1.0"
    kotlin("plugin.lombok") version "1.9.24"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    jacoco
}

version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":pdanet-model"))
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.4.4"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("io.micrometer:micrometer-registry-prometheus")

    implementation("org.springframework.boot:spring-boot-starter-mail:*")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:*")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:*")
    implementation("org.springframework.boot:spring-boot-starter-validation:*")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    implementation("net.lingala.zip4j:zip4j:2.11.5")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.729")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    // logging
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")

    // lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("org.apache.poi:poi-ooxml:5.2.2")

    // mapstruct
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    implementation("org.hibernate.orm:hibernate-core:6.6.4.Final")
    implementation("org.postgresql:postgresql:*")
    implementation("org.liquibase:liquibase-core")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:*")
    testImplementation("org.springframework.security:spring-security-test:*")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage", module = "junit-vintage-engine")
    }
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Test containers
    testImplementation("org.testcontainers:testcontainers:1.19.8")
    testImplementation(platform("org.testcontainers:testcontainers-bom:1.19.8"))
    testImplementation("org.testcontainers:postgresql:1.19.8")
    testImplementation("org.testcontainers:junit-jupiter:1.19.8")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    reports {
        csv.required = true
    }
    dependsOn(tasks.test) // tests are required to run before generating the report
}

detekt{
    autoCorrect = true
}

