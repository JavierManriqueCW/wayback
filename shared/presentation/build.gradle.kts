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
        iosMain {
           dependencies {
               implementation(libs.koin.core)
           }
        }

        androidMain {
            dependencies {
                implementation(compose.uiTooling)
                implementation(compose.preview)
                implementation(libs.koin.core.android)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.ktx)
                implementation(libs.androidx.runtime.android)
                implementation(libs.play.services.location)
            }
        }

        commonMain.dependencies {
            implementation(projects.shared.di.provider)
            implementation(projects.shared.di.core)
            implementation(projects.shared.common)
            implementation(projects.shared.domain)
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
            implementation(libs.compottie)
            implementation(libs.compottie.dot)
            implementation(libs.compottie.network)
            implementation(libs.compottie.resources)
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
        implementation(libs.androidx.activity.ktx)
        debugImplementation(compose.uiTooling)
        debugImplementation(compose.preview)
    }
}

dependencies {
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.runtime.android)
    debugImplementation(compose.uiTooling)
}
