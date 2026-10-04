plugins { `java-library` }
dependencies {
    api("org.springframework.boot:spring-boot")
    api("org.springframework:spring-context")
    api("org.springframework:spring-web")
    api("org.springframework:spring-tx")
    api("org.slf4j:slf4j-api")
    api("org.springframework.data:spring-data-commons")
    api("org.springframework.data:spring-data-jpa")
    api("io.swagger.core.v3:swagger-annotations-jakarta:2.2.43")
    api("org.hibernate.orm:hibernate-core")
    api("com.fasterxml.jackson.core:jackson-databind")
    api("jakarta.validation:jakarta.validation-api")
    api("org.springframework.modulith:spring-modulith-api:2.0.3")
    api("com.querydsl:querydsl-jpa:5.0.0:jakarta")
}
