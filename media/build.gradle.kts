plugins { `java-library` }
dependencies {
    api(project(":shared-kernel"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("software.amazon.awssdk:s3:2.25.30")
}
