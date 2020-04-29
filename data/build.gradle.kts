plugins {
    kotlin
}

dependencies {
    implementation(LibraryDependency.RETROFIT)
    implementation(LibraryDependency.COROUTINES_CORE)
    implementation(LibraryDependency.KOIN)

    // DOMAIN Module
    implementation(project(ModulesDependency.DOMAIN))
}
