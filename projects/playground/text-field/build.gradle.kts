plugins {
    id("java-se-convention")
}

dependencies {
    // external
    implementation(libs.tika) // TextFieldDemo -> *.io.FileUtils

    // internal
    implementation(project(":projects:mtbg"))
}