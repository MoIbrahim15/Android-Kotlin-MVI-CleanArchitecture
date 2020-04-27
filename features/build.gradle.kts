plugins {
    id(GradlePluginId.ANDROID_LIB)
    id(GradlePluginId.BASE_GRADLE_PLUGIN)
    id(GradlePluginId.SAFE_ARGS)
    `kotlin-kapt`
}

dependencies {

    // support
    implementation(LibraryDependency.APPCOMPAT)
    implementation(LibraryDependency.CORE_KTX)
    implementation(LibraryDependency.MATERIAL)
    implementation(LibraryDependency.CONSTRAINT)
    implementation(LibraryDependency.NAVIGATION_FRAGMENT)
    implementation(LibraryDependency.NAVIGATION_UI)
    implementation(LibraryDependency.NAVIGATION_RUNTIME)
    implementation(LibraryDependency.CROP)
    implementation(LibraryDependency.RECYCYLER_VIEW)
    implementation(LibraryDependency.CARD_VIEW)
    implementation(LibraryDependency.MATERIAL_DIALOG)
    implementation(LibraryDependency.GLIDE)
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")
    kapt(LibraryDependency.GLIDE_COMPILAR)

    implementation("com.squareup.okhttp3:okhttp:4.2.2")
    implementation(LibraryDependency.LIVE_DATA_RUNTIME)
    kapt(LibraryDependency.LIVE_DATA_COMPILER)
    implementation(LibraryDependency.LIVE_DATA_KTX)
    api(project(ModulesDependency.DOMAIN))

    addTestDependencies()
}
