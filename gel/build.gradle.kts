group = "tanvd"
version = "1.0-SNAPSHOT"

plugins {
    idea
    kotlin("jvm") version "1.3.21" apply true
}

idea {
    module {
        excludeDirs = files(
                ".gradle", ".gradle-cache", "gradle", "gradlew", "gradlew.bat", "gradle.properties",
                ".idea",
                "out", "build", "tmp"
        ).toHashSet()
        inheritOutputDirs = true
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib"))
    testCompile("org.junit.jupiter", "junit-jupiter-api", "5.2.0")
    testRuntime("org.junit.jupiter", "junit-jupiter-engine", "5.2.0")
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}

(tasks.findByName("wrapper") as Wrapper?)?.apply {
    gradleVersion = "5.1.1"
    distributionUrl = "https://cache-redirector.jetbrains.com/services.gradle.org/distributions/gradle-$gradleVersion-all.zip"
}