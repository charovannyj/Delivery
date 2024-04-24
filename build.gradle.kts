import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.0.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}

extra.apply{
    set("mapkitApiKey", getMapkitApiKey())
}

fun getMapkitApiKey(): String {
    val properties = Properties()
    rootProject.file("C:\\Users\\nikol\\AndroidStudioProjects\\Delivery\\app\\src\\main\\local.properties").inputStream().use {
        properties.load(it)
    }
    return properties.getProperty("MAPKIT_API_KEY", "")
}