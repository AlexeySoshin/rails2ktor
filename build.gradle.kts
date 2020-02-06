plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
    id("application")
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
    testCompile("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.6.0")
}

