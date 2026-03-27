import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("cerofiao.android.library")
                apply("cerofiao.android.compose")
                apply("cerofiao.android.hilt")
            }

            dependencies {
                add("implementation", project(":core:core-model"))
                add("implementation", project(":core:core-domain"))
                add("implementation", project(":core:core-ui"))
                add("implementation", project(":core:core-designsystem"))
                add("implementation", project(":core:core-common"))

                add("implementation", libs.findLibrary("androidx-navigation-compose").get())
                add("implementation", libs.findLibrary("hilt-navigation-compose").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                add("implementation", libs.findLibrary("kotlinx-serialization-json").get())
            }
        }
    }
}
