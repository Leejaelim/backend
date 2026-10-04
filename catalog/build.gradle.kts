plugins { `java-library` }
dependencies {
    api(project(":shared-kernel"))
    api(project(":media"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
