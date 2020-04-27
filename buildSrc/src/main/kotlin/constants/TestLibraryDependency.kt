object TestLibraryDependency{
    object Version{
        const val JUNIT = "4.13"
        const val JUNIT_ANDROID = "1.1.1"
        const val ESPRESSO = "3.2.0"
    }

    const val JUNIT = "junit:junit:${Version.JUNIT}"
    const val JUNIT_ANDROID = "androidx.test.ext:junit:${Version.JUNIT_ANDROID}"
    const val ESPRESSO = "androidx.test.espresso:espresso-core:${Version.ESPRESSO}"
    const val KOIN ="org.koin:koin-test:${LibraryDependency.Version.KOIN}"
}