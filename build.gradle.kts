plugins {
    alias(libs.plugins.gradle.android) apply false
    alias(libs.plugins.klibs.gradle.android.apk.name) apply false
    alias(libs.plugins.klibs.gradle.android.apk.sign) apply false
    alias(libs.plugins.klibs.gradle.android.compose) apply false
    alias(libs.plugins.klibs.gradle.android.namespace) apply false
    alias(libs.plugins.klibs.gradle.android.sdk) apply false
    alias(libs.plugins.klibs.rules.linter)
    alias(libs.plugins.klibs.gradle.dokka) apply false
    alias(libs.plugins.klibs.gradle.java.version) apply false
    alias(libs.plugins.klibs.gradle.publication) apply false
    alias(libs.plugins.klibs.gradle.rootinfo) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

tasks.withType<dev.detekt.gradle.Detekt>().configureEach {
    freeCompilerArgs.add("-Xexplicit-api=strict")
    baseline.set(rootProject.layout.projectDirectory.file("detekt-baseline.xml"))
}
