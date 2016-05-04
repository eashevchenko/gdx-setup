package com.github.czyzby.setup.data.gradle

import com.github.czyzby.setup.data.platforms.Android
import com.github.czyzby.setup.data.project.Project

/**
 * Gradle file of the root project. Manages build script and global settings.
 * @author MJ
 */
class RootGradleFile(val project: Project) : GradleFile("") {
    val plugins = mutableSetOf<String>()

    init {
        buildDependencies.add("\"com.badlogicgames.gdx:gdx-tools:\$gdxVersion\"")
    }

    override fun getContent(): String = """buildscript {
  repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven { url 'https://oss.sonatype.org/content/repositories/snapshots/' }
  }
  dependencies {
${joinDependencies(buildDependencies, type = "classpath", tab = "    ")}  }
}

allprojects {
  apply plugin: 'eclipse'
  apply plugin: 'idea'
}

configure(subprojects${if (project.hasPlatform(Android.ID)) {
        " - project(':android')"
    } else {
        ""
    }}) {
${plugins.joinToString(separator = "\n") { "  apply plugin: '$it'" }}
  sourceCompatibility = ${project.advanced.javaVersion}
}

subprojects {
  version = '${project.advanced.version}'
  ext.appName = '${project.basic.name}'
  repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven { url 'https://oss.sonatype.org/content/repositories/snapshots/' }
  }
}

// Clearing Eclipse project data in root folder:
tasks.eclipse.doLast {
  delete '.project'
  delete '.classpath'
  delete '.settings/'
}
"""

}