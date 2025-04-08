plugins {
    id("java")
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "ru.tbank"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.telegram:telegrambots:6.5.0")
    implementation("org.json:json:20241224")
    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.springframework:spring-context:6.2.0")
    implementation("org.springframework.boot:spring-boot:3.4.0")
    implementation("org.springframework.boot:spring-boot-autoconfigure:3.4.0")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.liquibase:liquibase-core:4.29.2")
    implementation ("org.springframework.boot:spring-boot-starter-webflux")

}

tasks.test {
    useJUnitPlatform()
}