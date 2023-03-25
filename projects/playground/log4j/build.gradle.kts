plugins {
    id("java-se-convention")
}

dependencies {
// external
    implementation(libs.bundles.log4j)

    // internal
    implementation(project(":projects:mtbg"))
}