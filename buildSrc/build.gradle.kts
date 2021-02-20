plugins {
    `kotlin-dsl`
}

//The kotlin-dsl plugin requires a repository to be declared
repositories {
    mavenCentral()
    google()
    jcenter()
}

gradlePlugin {
    plugins {
        register("base-gradle-plugin") {
            id = "base-gradle-plugin"
            implementationClass = "base_plugin.BaseGradlePlugin"
        }
    }
}


dependencies {
    /* Depend on the android gradle plugin, since we want to access it in our plugin */
    implementation("com.android.tools.build:gradle:7.0.0-alpha06")
    /* Depend on the kotlin plugin, since we want to access it in our plugin */
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
}
