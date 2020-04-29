import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(GradlePluginId.KTLINT_GRADLE) version CoreVersion.KTLINT_GRADLE
    id(GradlePluginId.DETEKT) version CoreVersion.DETEKT
}

buildscript {
    repositories {
        // Android plugin & support libraries
        google()

        // Main open-source repository
        jcenter()

        // Ktlint Gradle
        maven(GradlePluginId.KTLINT_MAVEN)
    }
    dependencies {
        classpath(GradleClasspath.ANDROID_GRADLE)
        classpath(kotlin(GradleClasspath.KOTLIN_PlUGIN, version = CoreVersion.KOTLIN))
        classpath(GradleClasspath.SAFE_ARGS)
        classpath(GradleClasspath.KTLINT_CLASSPATH)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }

    // We want to apply ktlint at all project level because it also checks build gradle files
    plugins.apply(GradlePluginId.KTLINT_GRADLE)
    // Ktlint configuration for sub-projects
    ktlint {
        version.set(CoreVersion.KTLINT)
        verbose.set(true)
        android.set(true)
        reporters {
            reporter(ReporterType.CHECKSTYLE)
        }
        ignoreFailures.set(true)
        filter {
            exclude("**/generated/**")
        }
    }

    plugins.apply(GradlePluginId.DETEKT)

    detekt {
        config = files("${project.rootDir}/config/detekt.yml")
        parallel = true
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
