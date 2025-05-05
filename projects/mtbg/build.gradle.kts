plugins {
    id("java-se-convention")
    //id("java-fx-convention")
    id("application")
    id("java-se-extra-module-info")
}

application {
    mainModule.set("net.ck.mtbg")
    mainClass.set("net.ck.mtbg.run.RunGame")
}

// https://docs.gradle.org/current/userguide/application_plugin.html
distributions {
    main {
        contents {
            // exclude empty jfx-modules
            exclude {
                it.name.matches(Regex("javafx-.*-\\d\\d.jar"))
            }
            // add assets near the start-scripts
            from(projectDir.resolve("assets")) {
                into("bin/assets")
            }
        }
    }
}

// https://docs.gradle.org/current/dsl/org.gradle.jvm.application.tasks.CreateStartScripts.html
tasks.getByName<CreateStartScripts>("startScripts") {
    defaultJvmOpts = listOf(
        "-splash:assets/graphics/splash/splash.jpg"

    )
}

// https://openjfx.io/openjfx-docs/#gradle
//javafx {
//    modules = listOf(
//        "javafx.media",
//        "javafx.swing"
//    )
//}

// for application run only
tasks.getByName<JavaExec>("run") {
    jvmArgs = listOf(
        "-splash:" + projectDir.absolutePath + "/assets/graphics/splash/splash.jpg", //, "quick", "startPosition:1@1"
        "-XX:StartFlightRecording"
    )
}

dependencies {
    // external
    implementation(libs.bundles.log4j)
    implementation(libs.google.guava)
    implementation(libs.locationtech)
    implementation(libs.greenrobot)
    implementation(libs.imgscalr)
    implementation(libs.opencsv)
    implementation(libs.picoli)
    implementation(libs.tika)
    //implementation(libs.bundles.openimaj)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // test
    testImplementation(libs.bundles.junit)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}