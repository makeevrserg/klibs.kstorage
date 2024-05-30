plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.gradle.android) apply false
    // klibs - core
    alias(libs.plugins.klibs.gradle.detekt) apply false
    alias(libs.plugins.klibs.gradle.detekt.compose) apply false
    alias(libs.plugins.klibs.gradle.dokka.root) apply false
    alias(libs.plugins.klibs.gradle.dokka.module) apply false
    alias(libs.plugins.klibs.gradle.java.core) apply false
    alias(libs.plugins.klibs.gradle.stub.javadoc) apply false
    alias(libs.plugins.klibs.gradle.publication) apply false
    alias(libs.plugins.klibs.gradle.rootinfo) apply false
    alias(libs.plugins.klibs.gradle.publication.signing) apply false
    // klibs - android
    alias(libs.plugins.klibs.gradle.android.core) apply false
    alias(libs.plugins.klibs.gradle.android.compose) apply false
    alias(libs.plugins.klibs.gradle.android.apk.sign) apply false
    alias(libs.plugins.klibs.gradle.android.apk.name) apply false
    alias(libs.plugins.klibs.gradle.android.publication) apply false
}

apply(plugin = "ru.astrainteractive.gradleplugin.dokka.root")
apply(plugin = "ru.astrainteractive.gradleplugin.detekt")
apply(plugin = "ru.astrainteractive.gradleplugin.root.info")

subprojects.forEach { subProject ->
    subProject.apply(plugin = "ru.astrainteractive.gradleplugin.dokka.module")
    subProject.apply(plugin = "ru.astrainteractive.gradleplugin.publication")
    subProject.afterEvaluate {
        subProject.apply(plugin = "ru.astrainteractive.gradleplugin.publication.kmp-signing")
    }
    subProject.plugins.withId("org.jetbrains.kotlin.jvm") {
        subProject.apply(plugin = "ru.astrainteractive.gradleplugin.java.core")
    }
    subProject.apply(plugin = "ru.astrainteractive.gradleplugin.stub.javadoc")
    subProject.plugins.withId("com.android.library") {
        subProject.apply(plugin = "ru.astrainteractive.gradleplugin.android.core")
    }
}
