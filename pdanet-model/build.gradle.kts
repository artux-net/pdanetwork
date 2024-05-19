import java.net.URI

plugins {
    id("java")
    id("maven-publish")
}

group = "net.artux.pdanetwork"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    // lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            version = System.getenv("GITHUB_REF_NAME")
            groupId = "net.artux.pdanetwork"
            artifactId = "pdanet-model"
        }
    }

    repositories {
        maven {
            val repository = System.getenv("GITHUB_REPOSITORY")
            name = "GitHubPackages"
            url = URI("https://maven.pkg.github.com/$repository/")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}