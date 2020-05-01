buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.Plugins.androidGradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}