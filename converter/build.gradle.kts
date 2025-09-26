plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.python)
}


android {
    namespace = "de.berlindroid.zepatch.converter"
    compileSdk = 36

    defaultConfig {
        minSdk = 25

        ndk {
            abiFilters += listOf("arm64-v8a", "x86_64")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
    }
}

chaquopy {
    defaultConfig {
        val pythonPath = System.getenv("ZEPATCH_PYTHON_PATH")
        if(pythonPath != null) {
            buildPython(pythonPath)
        }

        pip {
            install("pystitch==1.0.0")
        }

        version = "3.13"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.jts)
}
