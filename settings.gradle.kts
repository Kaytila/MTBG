rootProject.name = "net.ck.myTurnedBasedGame"

pluginManagement {
    resolutionStrategy {
        includeBuild("gradle/build-logic")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.4.0")
}

// order matters, if projects depend on each other
loadProjectsIn(rootProjectDir().resolve("projects"))
loadProjectsIn(rootProjectDir().resolve("projects/playground"))

for (project in rootProject.children) {
    project.projectDir = file("projects/${project.name}")
}

// load projects in given directory (no tree-walk)
// a project will be loaded if the directory contains a build.gradle.kts
fun loadProjectsIn (prjDir: File) {
    prjDir
        .listFiles()
        .toList()
        .filter { file -> file.resolve("build.gradle.kts").exists() }
        .forEach { file -> include(
            file.relativeTo(rootProjectDir()).toString().replace(File.separator, ":")
        ) }
}

// directory of root-project
fun rootProjectDir() : File {
    return rootProject.projectDir
}