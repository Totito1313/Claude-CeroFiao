import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val composeBom = libs.findLibrary("androidx-compose-bom").get()

            dependencies {
                add("implementation", platform(composeBom))
                add("implementation", libs.findLibrary("androidx-compose-ui").get())
                add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())
                add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
                add("implementation", libs.findLibrary("androidx-compose-material3").get())
                add("implementation", libs.findLibrary("androidx-compose-foundation").get())
                add("implementation", libs.findLibrary("androidx-compose-animation").get())
                add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
                add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())
            }
        }
    }
}

internal val Project.libs
    get() = extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java)
        .named("libs")
