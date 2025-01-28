plugins {
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.intellij") version "1.16.1"
    kotlin("plugin.serialization") version "1.9.20"
}

group = "com.github.michaeltomlinsontuks"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("io.ktor:ktor-client-core:2.3.6")
    implementation("io.ktor:ktor-client-cio:2.3.6")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.6")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}

intellij {
    version.set("2023.3")
    type.set("IU")
    plugins.set(listOf("JavaScript"))
    downloadSources.set(true)
    updateSinceUntilBuild.set(false)
}

tasks {
    buildSearchableOptions {
        enabled = false
    }

    patchPluginXml {
        version.set("${project.version}")
        sinceBuild.set("233")
        untilBuild.set("233.*")
    }
    
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}
