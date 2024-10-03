plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.chaw"
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
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")  // Spring MVC 추가
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")  // JPA 추가
    implementation("com.h2database:h2")  // H2 데이터베이스 추가
    implementation("org.projectlombok:lombok:1.18.30")  // Lombok 추가
    implementation("org.springframework.boot:spring-boot-starter-validation")  // Validation 추가
    implementation("org.modelmapper:modelmapper:3.1.0") // ModelMapper 추가
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")  // Swagger OpenAPI 추가

    compileOnly("org.projectlombok:lombok")  // Lombok 애너테이션 처리용
    annotationProcessor("org.projectlombok:lombok")  // Lombok 컴파일 타임에 사용

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
