plugins { `java-library` }
dependencies {
    api(project(":shared-kernel"))
    api(project(":catalog"))
    api(project(":identity"))
    api(project(":recommendation"))
    api(project(":media"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
