plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    `maven-publish`
    signing
}

android {
    namespace = "io.github.xiaobaicz.initializer"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.auto.service.annotations)
    kapt(libs.auto.service)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.xiaobaicz"
            artifactId = "initializer"
            version = "0.0.1"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name = "initializer"
                description = "android initializer"
                url = "https://github.com/xiaobaicz/initializer"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        name = "bocheng.lao"
                        email = "xiaojinjincz@outlook.com"
                        organization = "bocheng.lao"
                        organizationUrl = "https://xiaobaicz.github.io"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/xiaobaicz/initializer.git"
                    developerConnection = "scm:git:https://github.com/xiaobaicz/initializer.git"
                    url = "https://github.com/xiaobaicz/initializer"
                }
            }
        }
    }
    repositories {
        maven {
            url = uri("../build/maven")
        }
    }
}

signing {
    sign(publishing.publications["release"])
}