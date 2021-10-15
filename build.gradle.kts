plugins {
    java
    id("java-library")
    kotlin("multiplatform") version "1.5.31" apply true
    id("io.kotest.multiplatform") version "5.0.0.5" apply true
}

group = "com.m2f"
version =  "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {

    targets {
        jvm {
            compilations.all {
                kotlinOptions {
                    jvmTarget = "1.8"
                }
            }
        }
        js(IR) {
            browser()
            nodejs()
        }
    }

    targets.all {
        compilations.all {
            kotlinOptions {
                verbose = true
            }
        }
    }


    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("io.arrow-kt:arrow-core:1.0.0")
                implementation("io.arrow-kt:arrow-optics:1.0.0")
                implementation("io.arrow-kt:arrow-fx-coroutines:1.0.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.kotest:kotest-property:5.0.0.M2")
                implementation("io.kotest:kotest-framework-engine:5.0.0.M1")
                implementation("io.kotest:kotest-assertions-core:5.0.0.M2")
                implementation("io.mockk:mockk-common:1.12.0")
                implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
            }
        }

        val jsTest by getting {
            dependencies {
                implementation("io.kotest:kotest-framework-engine:5.0.0.M2")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.arrow-kt:arrow-core-jvm:1.0.0")

            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5:5.0.0.M2")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        apiVersion = "1.5"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    filter {
        isFailOnNoMatchingTests = false
    }
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}