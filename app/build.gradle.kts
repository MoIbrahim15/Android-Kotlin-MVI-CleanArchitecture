plugins {
    id(GradlePluginId.ANDROID_APP)
    id(GradlePluginId.BASE_GRADLE_PLUGIN)
}

android {
    defaultConfig {
        applicationId = AndroidConfig.ID
    }
}

dependencies {
    implementation(project(ModulesDependency.CACHE))
    implementation(project(ModulesDependency.REMOTE))
    implementation(project(ModulesDependency.DATA))
    implementation(project(ModulesDependency.DOMAIN))
    implementation(project(ModulesDependency.FEATURES))
}
