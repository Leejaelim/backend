plugins { `java-library` }
dependencies {
    api(project(":shared-kernel"))
    api(project(":identity"))
    api(project(":group-decision"))
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
}
