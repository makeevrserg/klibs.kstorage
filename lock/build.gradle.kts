@file:Suppress("UnusedPrivateMember")

plugins {
    kotlin("multiplatform")
    id("ru.astrainteractive.gradleplugin.publication")
}

kotlin {
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
