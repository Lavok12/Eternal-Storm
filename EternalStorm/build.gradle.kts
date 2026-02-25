plugins {
    kotlin("jvm") version "2.1.20"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Стандартная рефлексия Kotlin (обязательно для работы с KClass, аннотациями и свойствами)
    implementation(kotlin("reflect"))

    // Библиотека для сканирования пакетов (поиск классов с твоей аннотацией)
    implementation("org.reflections:reflections:0.10.2")

    // Твои текущие зависимости
    implementation(fileTree("libs") { include("*.jar") })
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.9.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    // 👇 указываем имя класса с main
    mainClass.set("la.vok.MainKt")
}
