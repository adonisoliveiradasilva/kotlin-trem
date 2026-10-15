
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "com.trem.AppKt"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(libs.logback.classic)
    implementation("org.springframework:spring-jdbc:6.1.13")
    implementation("org.postgresql:postgresql:42.7.4")
    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}