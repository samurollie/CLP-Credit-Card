plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("application") // Add this line
	id("com.bmuschko.docker-spring-boot-application") version "9.3.1"
}

group = "com.clp"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	implementation("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.jetbrains.exposed:exposed-core:0.55.0")
	implementation("org.jetbrains.exposed:exposed-dao:0.55.0")
	implementation("org.jetbrains.exposed:exposed-jdbc:0.55.0")
	implementation("org.jetbrains.exposed:exposed-java-time:0.55.0")

	implementation("org.jetbrains.exposed:exposed-crypt:0.55.0")
	implementation("org.jetbrains.exposed:exposed-json:0.55.0")
	implementation("org.jetbrains.exposed:exposed-money:0.55.0")
	implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.55.0")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

application {
	mainClass.set("com.clp.credit_card.CreditCardApplicationKt")
}

springBoot {
	mainClass.set("com.clp.credit_card.CreditCardApplicationKt")
}

tasks.bootJar {
	archiveFileName.set("credit_card.jar") // Nome do arquivo JAR
}

docker {
	springBootApplication {
		baseImage.set("openjdk:17-jdk-slim")
		ports.set(listOf(8080))
		images.set(setOf("${project.group}/${project.name}:${project.version}"))
	}
}