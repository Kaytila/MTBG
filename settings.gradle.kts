rootProject.name = "net.ck.myTurnedBasedGame"

pluginManagement {
    resolutionStrategy {
        includeBuild("gradle/build-logic")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.4.0")
}

include("projects:mtbg")
include("projects:mtbg-tools")
include("projects:playground")

for (project in rootProject.children) {
    project.projectDir = file("projects/${project.name}")
}