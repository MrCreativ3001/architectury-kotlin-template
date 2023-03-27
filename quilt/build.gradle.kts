plugins {
    id("com.github.johnrengelman.shadow")
}

repositories {
    maven {
        url = uri("https://maven.quiltmc.org/repository/release/")
    }
}

architectury {
    platformSetupLoomIde()
    loader("quilt")
}

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating
val developmentQuilt: Configuration by configurations.getting

configurations {
    compileClasspath.extendsFrom(common)
    runtimeClasspath.extendsFrom(common)
    developmentQuilt.extendsFrom(common)
}

dependencies {
    modImplementation("org.quiltmc:quilt-loader:${rootProject.property("quilt_loader_version")}")
    modApi("org.quiltmc.quilted-fabric-api:quilted-fabric-api:${rootProject.property("quilt_fabric_api_version")}")
    // Remove the next few lines if you don't want to depend on the API
    modApi("dev.architectury:architectury-fabric:${rootProject.property("architectury_version")}") {
        // We must not pull Fabric Loader from Architectury Fabric
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
    }
    modApi("org.quiltmc:qsl:${rootProject.property("quilt_standard_library_version")}")

    common(project(":common", "namedElements")) {
        isTransitive = false
    }
    shadowCommon(project(":common", "transformProductionQuilt")){
        isTransitive = false
    }
    common(project(":fabric-like", "namedElements")){
        isTransitive = false
    }
    shadowCommon(project(":fabric-like", "transformProductionQuilt")) {
        isTransitive = false
    }

    modApi("org.quiltmc.quilt-kotlin-libraries:quilt-kotlin-libraries:${rootProject.property("quilt_kotlin_libraries_version")}")
}

tasks.processResources {
    inputs.property("group", rootProject.property("maven_group"))
    inputs.property("version", project.version)

    filesMatching("quilt.mod.json") {
        expand(mutableMapOf(
            Pair("group", rootProject.property("maven_group")),
            Pair("version", project.version),

            Pair("mod_id", rootProject.property("mod_id")),
            Pair("minecraft_version", rootProject.property("minecraft_version")),
            Pair("architectury_version", rootProject.property("architectury_version"))
        ))
    }
}

tasks.shadowJar {
    exclude("architectury.common.json")
    configurations = listOf(shadowCommon)
    classifier = "dev-shadow"
}

tasks.remapJar {
    injectAccessWidener.set(true)
    input.set(tasks.shadowJar.get().archiveFile)
    dependsOn(tasks.shadowJar)
    classifier = null
}

tasks.jar {
    classifier = "dev"
}

tasks.sourcesJar {
    val commonSources = project(":common").tasks.getByName<Jar>("sourcesJar")
    dependsOn(commonSources)
    from(commonSources.archiveFile.map { zipTree(it) })
}

components.getByName("java") {
    this as AdhocComponentWithVariants
    this.withVariantsFromConfiguration(project.configurations["shadowRuntimeElements"]) {
        skip()
    }
}