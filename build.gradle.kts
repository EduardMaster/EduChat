plugins {
    java
    kotlin("jvm") version "1.6.0"
}

group = "net.eduard"
version = "1.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io/")
}
dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("net.eduard:eduardapi:1.0-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

}
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileJava{
        options.encoding = "UTF-8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    jar {
        destinationDirectory.set(file("E:\\Tudo\\Minecraft - Server\\Servidor Teste\\plugins\\"))
    }
}
