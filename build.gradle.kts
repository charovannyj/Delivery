import java.util.Properties
buildscript{
    val kotlin_version by extra("1.8.20")
    repositories{
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.0.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false


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