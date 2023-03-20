plugins {
    id("java-se-convention")
    id("java-fx-convention")
    id("application")
    id("java-se-extra-module-info")
}

application {
    mainModule.set("net.ck.mtbg")
    mainClass.set("net.ck.game.run.RunGame")
}

// https://docs.gradle.org/current/userguide/application_plugin.html
distributions {
    main {
        contents {
            // exclude empty jfx-modules
            exclude {
                it.name.matches(Regex("javafx-.*-\\d\\d.jar"))
            }
            from(projectDir.resolve("splash")) {
                into("bin")
            }
        }
    }
}

// https://docs.gradle.org/current/dsl/org.gradle.jvm.application.tasks.CreateStartScripts.html
tasks.getByName<CreateStartScripts>("startScripts") {
    defaultJvmOpts = listOf(
        "-splash:splash.jpg"
    )
}

// https://openjfx.io/openjfx-docs/#gradle
javafx {
    modules = listOf(
        "javafx.media",
        "javafx.swing"
    )
}

// https://docs.oracle.com/javase/7/docs/api/java/awt/SplashScreen.html
tasks.getByName<Jar>("jar") {
    manifest {
        attributes["SplashScreen-Image"] = "splash.jpg"
    }
}

// for application run only
tasks.getByName<JavaExec>("run") {
    jvmArgs = listOf(
        "-splash:" + projectDir.absolutePath + "/splash/splash.jpg"
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

    // test
    testImplementation(libs.bundles.junit)
}