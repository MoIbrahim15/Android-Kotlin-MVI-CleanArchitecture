object AndroidConfig {

    const val COMPILE_SDK_VERSION = 30
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 30
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1"

    const val ID = "com.mi.MVI"
    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
}


interface BuildType {

    companion object {
        const val RELEASE = "release"
        const val DEBUG = "release"
    }

    val isMinifyEnabled: Boolean
}

object BuildTypeDebug : BuildType {
    override val isMinifyEnabled = false
}

object BuildTypeRelease : BuildType {
    override val isMinifyEnabled = true
}