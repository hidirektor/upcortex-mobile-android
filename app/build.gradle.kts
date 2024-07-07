import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

val keystorePropertiesFile = file("signing.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "me.t3sl4.upcortex"
    compileSdk = 34

    applicationVariants.all {
        outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            val variantName = name
            val versionName = versionName
            val formattedDate = SimpleDateFormat("dd-MM-yyyy").format(Date())
            val fileExtension = output.outputFile.extension
            output.outputFileName = "UpCortex ${variantName}_${formattedDate}_v${versionName}.${fileExtension}"
        }
    }

    val versionPropsFile = file("version.properties")
    val versionProps = Properties()

    if (versionPropsFile.canRead()) {
        versionProps.load(FileInputStream(versionPropsFile))

        val patch = versionProps["PATCH"].toString().toInt() + 1
        var minor = versionProps["MINOR"].toString().toInt()
        var major = versionProps["MAJOR"].toString().toInt()
        val realVersionCode = versionProps["VERSION_CODE"].toString().toInt() + 1

        if (patch == 100) {
            minor += 1
            versionProps["PATCH"] = "0"
        } else {
            versionProps["PATCH"] = patch.toString()
        }

        if (minor == 10) {
            major += 1
            minor = 0
        }

        versionProps["MINOR"] = minor.toString()
        versionProps["MAJOR"] = major.toString()
        versionProps["VERSION_CODE"] = realVersionCode.toString()
        versionProps.store(versionPropsFile.writer(), null)

        defaultConfig {
            applicationId = "me.t3sl4.upcortex"
            minSdk = 29
            targetSdk = 34
            versionCode = realVersionCode
            versionName = "$major.$minor.$patch($versionCode)"

            buildConfigField("String", "BASE_URL", "\"http://160.20.111.42:3200/api/\"")

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        signingConfigs {
            create("key0") {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    } else {
        throw GradleException("Could not read version.properties!")
    }

    sourceSets["main"].res.srcDirs(
        "src/main/res/layouts/merchant",
        "src/main/res/layouts/dashboard",
        "src/main/res/layouts/order",
        "src/main/res/layouts/earning",
        "src/main/res/layouts/profile",
        "src/main/res/layouts/hamburger",
        "src/main/res/layouts/general",
        "src/main/res/layouts/onboard",
        "src/main/res/layouts/auth",
        "src/main/res/layouts",
        "src/main/res",
        "src/main/res/layouts/dialogs",
        "src/main/res/layouts/diyalog"
    )

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            buildFeatures.buildConfig = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:deprecation")
    options.isWarnings = true
}

dependencies {
    implementation(libs.appcompat.v170)
    implementation(libs.constraintlayout)
    implementation(libs.security.crypto)

    implementation(libs.material)
    implementation(libs.protolite.well.known.types)
    implementation(libs.play.services.base)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.android.maps.utils)
    implementation(libs.gson)
    implementation("com.google.code.findbugs:annotations:3.0.1") {
        exclude(group = "com.google.code.findbugs", module = "jsr305")
        exclude(group = "net.jcip", module = "jcip-annotations")
    }

    implementation(libs.core.ktx)

    implementation(libs.simpleratingbar)
    implementation(libs.zcheckbox)
    implementation(libs.ccp)
    implementation(libs.pinview)
    implementation(libs.circleimageview)

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}