
plugins {
    id(GradlePluginId.ANDROID_LIB) // cache must be android lib for RoomDatabase
    id(GradlePluginId.BASE_GRADLE_PLUGIN)
    `kotlin-kapt`
}

dependencies {
    implementation(LibraryDependency.ROOM_RUNTIME)
    kapt(LibraryDependency.ROOM_COMPILER)
    implementation(LibraryDependency.ROOM_KTX)
    implementation(project(ModulesDependency.DATA))
}
