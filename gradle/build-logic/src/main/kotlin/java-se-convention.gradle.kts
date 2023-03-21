import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java")
}

group = "net.ck"
version = "0.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    testLogging.events = setOf(
        TestLogEvent.PASSED,
        TestLogEvent.FAILED,
        TestLogEvent.SKIPPED
    )
}