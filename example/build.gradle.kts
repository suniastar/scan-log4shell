/*
 * Copyright (c) 2021. heddier electronic GmbH - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * Written by Frederik Enste <fenste@heddier.com>
 */

import java.time.Instant

val coroutinesVersion: String by project
val junitVersion: String by project

fun getJavaVersion(): String = System.getProperty("java.version")
fun getJavaVendor(): String = System.getProperty("java.vendor")
fun getJvmVersion(): String = System.getProperty("java.vm.version")

description = "An example affected by the log4shell cve"
group = rootProject.group
version = rootProject.version

plugins {
    kotlin("jvm")
    id("application")
    id("com.github.johnrengelman.shadow")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.dokka")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    // THIS ONE IS AFFECTED AS WELL AS 2.15 YOU SHOULD UPGRADE TO 2.16 OR HIGHER
    implementation("org.apache.logging.log4j", "log4j-core", "2.14.1")
}

application {
    mainClass.set("de.fenste.log4shell.example.MainKt")
    mainClassName = mainClass.get() // requirement for shadowJar
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

detekt {
    allRules = false
    buildUponDefaultConfig = true
    config = files("${rootProject.projectDir}/detekt.yaml")
}

ktlint {
    filter {
        exclude("**/generated/**")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = java.targetCompatibility.toString()
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
    }
}

tasks.withType<Jar> {
    archiveBaseName.set("scan-log4shell-vuln-thin")
    manifest.attributes(
        mapOf(
            "Build-By" to "Frederik Enste <fenste@heddier.com>",
            "Build-Jdk" to "${getJavaVersion()} (${getJavaVendor()} ${getJvmVersion()})",
            "Build-Timestamp" to Instant.now().toString(),
            "Created-By" to "Gradle ${gradle.gradleVersion}",
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Main-Class" to application.mainClass.get()
        )
    )
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveBaseName.set("scan-log4shell-vuln--fat")
    mergeServiceFiles()
    manifest.attributes(
        mapOf(
            "Build-By" to "Frederik Enste <fenste@heddier.com>",
            "Build-Jdk" to "${getJavaVersion()} (${getJavaVendor()} ${getJvmVersion()})",
            "Build-Timestamp" to Instant.now().toString(),
            "Created-By" to "Gradle ${gradle.gradleVersion}",
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version
        )
    )
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    javadoc {
        dependsOn(dokkaJavadoc)
    }
}
