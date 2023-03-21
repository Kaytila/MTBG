plugins {
    id("java-se-convention")
}

dependencies {
    // external
    implementation(libs.bundles.log4j)
    implementation(libs.tika) // TextFieldDemo -> *.io.FileUtils

    // internal
    implementation(project(":projects:mtbg"))
}