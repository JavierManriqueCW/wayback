import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {

    androidTarget()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "SharedApp"
            isStatic = true
        }
    }

    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }
    
    sourceSets {
        androidMain {
            dependencies {
                implementation(compose.uiTooling)
                implementation(compose.preview)
                implementation(libs.koin.core.android)
            }
        }

        commonMain.dependencies {
            implementation(projects.sharedApp.di.provider)
            implementation(projects.sharedApp.di.core)
            implementation(projects.sharedApp.common)
            implementation(projects.sharedApp.domain)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.navigation.compose)
            implementation(libs.compose.shimmer)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }
    }
}

android {
    namespace = "com.jmp.wayback.presentation"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/composeResources")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    dependencies {
        debugImplementation(compose.uiTooling)
        debugImplementation(compose.preview)
    }
}

dependencies {
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.runtime.android)
    implementation(project(":sharedApp:domain"))
    debugImplementation(compose.uiTooling)
}
