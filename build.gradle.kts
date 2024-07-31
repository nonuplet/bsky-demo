import java.util.Properties


plugins {
    kotlin("jvm") version "2.0.0"
}

group = "com.rirfee"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.repsy.io/mvn/uakihir0/public") }
}

dependencies {
    implementation("work.socialhub.kbsky:core:0.0.1-SNAPSHOT")
    implementation("work.socialhub.kbsky:stream:0.0.1-SNAPSHOT")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
