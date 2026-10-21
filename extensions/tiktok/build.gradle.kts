dependencies {
    compileOnly(project(":extensions:tiktok:stub"))

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.launcher)
}

extension {
    name = "extensions/tiktok.mpe"
}

android {
    namespace = "app.unscroll.extension.tiktok"

    defaultConfig {
        minSdk = 23
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.all { it.useJUnitPlatform() }
    }
}
