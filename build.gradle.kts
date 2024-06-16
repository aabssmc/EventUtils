import xyz.srnyx.gradlegalaxy.enums.Repository
import xyz.srnyx.gradlegalaxy.enums.repository
import xyz.srnyx.gradlegalaxy.utility.addReplacementsTask
import xyz.srnyx.gradlegalaxy.utility.getDefaultReplacements
import xyz.srnyx.gradlegalaxy.utility.setupJava

plugins {
    java
    id("fabric-loom") version "1.6.9"
    id("xyz.srnyx.gradle-galaxy") version "1.1.3"
}

var modVersion = "1.4"
setupJava("cc.aabss", "1.20.1-${project.version}", "Alerting for Event Alerts Minecraft events")

addReplacementsTask(setOf("fabric.mod.json"), getDefaultReplacements() + mapOf("mod_version" to modVersion))

repository(Repository.MAVEN_CENTRAL, Repository.JITPACK)
repository("https://maven.shedaniel.me", "https://maven.fabricmc.net", "https://maven.terraformersmc.com/releases")

dependencies {
    minecraft("com.mojang", "minecraft", "1.20.1")
    mappings("net.fabricmc:yarn:1.20.1+build.10:v2")
    modImplementation("net.fabricmc", "fabric-loader", "0.15.11")
    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.92.1+1.20.1")
    modApi("me.shedaniel.cloth", "cloth-config-fabric", "11.1.118")
    modApi("com.terraformersmc", "modmenu", "7.1.0")
    include(implementation("com.github.NepNep21", "DiscordRPC4j16", "1.2.2"))
}