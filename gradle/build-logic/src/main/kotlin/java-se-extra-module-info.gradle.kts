plugins {
    id("java")
    id("org.gradlex.extra-java-module-info")
}

extraJavaModuleInfo {
    failOnMissingModuleInfo.set(false)
    automaticModule("eventbus-java-3.3.1.jar", "eventbus.java")
    automaticModule("imgscalr-lib-4.2.jar", "imgscalr.lib")
}