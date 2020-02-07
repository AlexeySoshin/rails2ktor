import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.platform.jvm.JvmPlatforms

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
    application
}


repositories {
    mavenCentral()
    jcenter()
}

application {
    mainClassName = "MainKt"
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.ktor:ktor-server-core:1.3.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
    useJUnitPlatform()
}