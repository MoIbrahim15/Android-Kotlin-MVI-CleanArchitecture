package base_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

open class BaseGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.configureDefaultPlugins()
        project.configureAndroidApp()
        project.configureDependencies()
    }
}