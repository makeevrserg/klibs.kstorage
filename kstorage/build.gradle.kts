@file:Suppress("UnusedPrivateMember")

import ru.astrainteractive.gradleplugin.property.extension.ModelPropertyValueExt.requireProjectInfo


plugins {
    kotlin("multiplatform")
    id("com.android.library")
}
kotlin {
    jvm()
    androidTarget()
    js(IR) {
        browser()
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
    wasmJs {
        browser()
    }

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
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.androidx.datastore.preferences.core)
                implementation(libs.androidx.datastore.core.okio)
            }
        }
        val wasmJsMain by getting
        val jsMain by getting
        val webMain by getting
        val nonJsMain by creating {
            this.dependsOn(commonMain)
            sourceSets.toList()
                .filter { sourceSet -> sourceSet.name.endsWith("Main") }
                .filter { sourceSet -> sourceSet.name != wasmJsMain.name }
                .filter { sourceSet -> sourceSet.name != jsMain.name }
                .filter { sourceSet -> sourceSet.name != webMain.name }
                .filter { sourceSet -> sourceSet.name != commonMain.name }
                .onEach { sourceSet -> sourceSet.dependsOn(this) }
                .toList()
        }
    }
}

android {
    namespace = "${requireProjectInfo.group}.kstorage"
}
