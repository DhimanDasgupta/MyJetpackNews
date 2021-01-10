package com.buildsrc

object App {
    const val applicationId = "com.dhimandasgupta.myjetpacknews"
    const val versionCode = 1
    const val versionName = "1.0"
}

object Versions {
    const val minSdk = 22
    const val targetSdk = 30
    const val compileSdk = 30
    const val buildTool = "30.0.2"
    const val kotlin = Deps.Kotlin.version
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.0-alpha03"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Hilt.version}"

    object Kotlin {
        const val version = "1.4.21"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
        const val common = "org.jetbrains.kotlin:kotlin-stdlib-common:$version"

        const val compilerVersion = "1.4.21"

        object Coroutine {
            private const val version = "1.4.2"

            const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
            const val coroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }

        object MockK {
            private const val version = "1.10.2"

            const val mockk = "io.mockk:mockk:$version"
        }
    }

    object Okhttp {
        private const val version = "4.9.0"

        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object Retrofit {
        private const val version = "2.9.0"

        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val converter = "com.squareup.retrofit2:converter-gson:$version"
    }

    object Hilt {
        const val version = "2.29-alpha"

        const val kapt = "com.google.dagger:hilt-android-compiler:$version"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
    }

    object ThreeTenAndroid {
        private const val version = "1.3.0"

        const val threeTenABP = "com.jakewharton.threetenabp:threetenabp:$version"
    }

    object HiltAndroid {
        private const val version = "1.0.0-alpha02"

        const val kapt = "androidx.hilt:hilt-compiler:$version"
        const val viewModel = "androidx.hilt:hilt-lifecycle-viewmodel:$version"
    }

    object AndroidX {
        private const val androidXCoreVersion = "1.3.2"
        private const val androidXVersion = "1.1.0"
        private const val viewModelKtxVersion = "2.2.0"

        const val coreKtx = "androidx.core:core-ktx:$androidXCoreVersion"
        const val appcompat = "androidx.appcompat:appcompat:$androidXVersion"
        const val activityKtx = "androidx.activity:activity-ktx:$androidXVersion"
        const val material = "com.google.android.material:material:$androidXVersion"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$viewModelKtxVersion"

        object Compose {
            private const val version = "1.0.0-alpha09"

            const val ui = "androidx.compose.ui:ui:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val foundation = "androidx.compose.foundation:foundation-layout:$version"
            const val material = "androidx.compose.material:material:$version"
            const val animationCore = "androidx.compose.animation:animation-core:$version"
            const val animation = "androidx.compose.animation:animation:$version"
            const val composer = "androidx.compose:compose-compiler:$version"
            const val savedInstanceState = "androidx.compose.runtime:runtime-saved-instance-state:$version"
            const val liveData = "androidx.compose.runtime:runtime-livedata:$version"

            object Navigation {
                private const val version = "1.0.0-alpha03"

                const val navigation = "androidx.navigation:navigation-compose:$version"
            }
        }

        object Accompanist {
            private const val version = "0.4.1"

            const val accompanistCoil = "dev.chrisbanes.accompanist:accompanist-coil:$version"
            const val accompanistMdc = "dev.chrisbanes.accompanist:accompanist-mdc-theme:$version"
            const val accompanistWindowInset = "dev.chrisbanes.accompanist:accompanist-insets:$version"
        }

        object Lifecycle {
            private const val version = "2.3.0-beta01"

            const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }

        const val AndroidXCoreTesting = "androidx.arch.core:core-testing:2.1.0"
    }

    object MicrosoftDuo {
        const val SDk = "https://pkgs.dev.azure.com/MicrosoftDeviceSDK/DuoSDK-Public/_packaging/Duo-SDK-Feed/maven/v1"

        private const val dualScreenLayoutVersion = "1.0.0-alpha01"

        const val duoCore = "com.microsoft.device.dualscreen:core:$dualScreenLayoutVersion"
        const val duoLayout = "com.microsoft.device.dualscreen:layouts:1.0.0-alpha02" // Not equal with core
    }
}
