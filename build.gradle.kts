import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("org.jetbrains.compose") version "1.2.2"
}

group = "com.namtarr"
version = "1.0.1"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        tasks.named<Test>("jvmTest") {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.jetbrains.compose.compiler:compiler:1.3.2")
                implementation("dev.burnoo:cokoin:1.0.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.junit.jupiter:junit-jupiter:5.8.1")
                implementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
            }
        }
    }
}

compose {
    desktop {
        application {
            mainClass = "MainKt"
            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
                packageName = "aab-installer"
                packageVersion = "1.0.1"
            }
        }
    }
}