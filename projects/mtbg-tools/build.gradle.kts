plugins {
    id("java-se-convention")
    id("application")
}

dependencies {
    // external
    implementation(libs.bundles.log4j)

    // internal
    implementation(project(":projects:mtbg"))
}