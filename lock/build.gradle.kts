@file:Suppress("UnusedPrivateMember")

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("ru.astrainteractive.gradleplugin.publication")
    id("ru.astrainteractive.klibs.rules.gradle.linter")
    id("ru.astrainteractive.gradleplugin.rootinfo")
}

linter {
    unitKind.set("library")
    rootPackage.set("ru.astrainteractive.klibs.kstorage")
}

kotlin {
    explicitApi()
    compilerOptions {
        optIn.add("ru.astrainteractive.klibs.kstorage.lock.api.InternalLockApi")
    }
    jvm()
    js(IR) {
        browser { testTask { isEnabled = false } }
        nodejs()
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()
    watchosX64()
    watchosArm64()
    watchosSimulatorArm64()
    linuxX64()
    macosX64()
    macosArm64()
    mingwX64()
    applyDefaultHierarchyTemplate()
    wasmJs { browser { testTask { isEnabled = false } } }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlin.coroutines.test)
                implementation(libs.settings)
                implementation(libs.settings.observable)
                implementation(libs.settings.coroutines)
            }
        }
    }
}

tasks.withType<dev.detekt.gradle.Detekt>().configureEach {
    freeCompilerArgs.add("-Xexplicit-api=strict")
    baseline.set(rootProject.layout.projectDirectory.file("detekt-baseline.xml"))
}
