plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // https://plugins.gradle.org/plugin/org.gradlex.extra-java-module-info
    implementation("org.gradlex:extra-java-module-info:1.3")
    // https://plugins.gradle.org/plugin/org.openjfx.javafxplugin
    // implementation("org.openjfx.javafxplugin:org.openjfx.javafxplugin.gradle.plugin:0.0.13")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}