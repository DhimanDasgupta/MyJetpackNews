package com.buildsrc

object App {
    const val applicationId = "com.dhimandasgupta.myjetpacknews"
    const val versionCode = 1
    const val versionName = "1.0"
}

object Versions {
    const val minSdk = 21
    const val targetSdk = 30
    const val compileSdk = 30
    const val buildTool = "30.0.2"
    const val kotlin = Deps.Kotlin.version
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:4.2.0-alpha07"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Hilt.version}"

    object Kotlin {
        const val version = "1.4.0"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
        const val common = "org.jetbrains.kotlin:kotlin-stdlib-common:$version"

        const val compilerVersion = "1.4.0-dev-withExperimentalGoogleExtensions-20200720"

        object Coroutine {
            private const val version = "1.3.9"

            const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        }
    }

    object Retrofit {
        private const val version = "2.9.0"

        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val converter = "com.squareup.retrofit2:converter-gson:$version"
    }

    object Hilt {
        const val version = "2.28-alpha"

        const val kapt = "com.google.dagger:hilt-android-compiler:$version"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
    }

    object HiltAndroid {
        private const val version = "1.0.0-alpha02"

        const val kapt = "androidx.hilt:hilt-compiler:$version"
        const val viewModel = "androidx.hilt:hilt-lifecycle-viewmodel:$version"
    }

    object AndroidX {
        private const val androidXCoreVersion = "1.3.0"
        private const val androidXVersion = "1.1.0"

        const val coreKtx = "androidx.core:core-ktx:$androidXCoreVersion"
        const val appcompat = "androidx.appcompat:appcompat:$androidXVersion"
        const val activityKtx = "androidx.activity:activity-ktx:$androidXVersion"
        const val material = "com.google.android.material:material:$androidXVersion"

        object Compose {
            private const val version = "0.1.0-dev17"

            const val ui = "androidx.compose.ui:ui:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val foundation = "androidx.compose.foundation:foundation-layout:$version"
            const val material = "androidx.compose.material:material:$version"
            const val animationCore = "androidx.compose.animation:animation-core:$version"
            const val animation = "androidx.compose.animation:animation:$version"
            const val tooling = "androidx.ui:ui-tooling:$version"
            const val composer = "androidx.compose:compose-compiler:$version"
            const val savedInstanceState = "androidx.compose.runtime:runtime-saved-instance-state:$version"
            const val liveData = "androidx.compose.runtime:runtime-livedata:$version"
        }

        object Accompanist {
            private const val version = "0.1.9"

            const val accompanistCoil = "dev.chrisbanes.accompanist:accompanist-coil:$version"
            const val accompanistMdc = "dev.chrisbanes.accompanist:accompanist-mdc-theme:$version"
        }

        object Lifecycle {
            private const val version = "2.3.0-alpha07"

            const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }
    }
}
