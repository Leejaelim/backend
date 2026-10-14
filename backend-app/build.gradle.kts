import org.springframework.boot.gradle.tasks.bundling.BootJar
plugins { java; id("org.springframework.boot") }
dependencies {
    api(project(":shared-kernel"))
    api(project(":identity"))
    api(project(":catalog"))
    api(project(":recommendation"))
    api(project(":group-decision"))
    api(project(":media"))
    api(project(":realtime"))
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-oauth2-client")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-webmvc")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-actuator")
    api("org.springframework.boot:spring-boot-starter-mail")
    api("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2")
    implementation("net.ttddyy:datasource-proxy:1.10")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
}
tasks.named<BootJar>("bootJar") {
    archiveBaseName.set("backend")
    destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
}
