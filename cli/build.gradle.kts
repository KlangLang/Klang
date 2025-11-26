plugins {
    application
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":stdlib"))

    implementation("info.picocli:picocli:4.7.5")
    annotationProcessor("info.picocli:picocli-codegen:4.7.5")
}

application {
    mainClass.set("org.klang.cli.KMain")
}

tasks {
    shadowJar {
        archiveBaseName.set("k")         
        archiveClassifier.set("")        
        mergeServiceFiles()              

        manifest {
            attributes(
                "Main-Class" to "org.klang.cli.KMain",
                "Implementation-Version" to project.version
            )
        }
    }

    build {
        dependsOn(shadowJar)             
    }

    jar {
        enabled = false                  
    }
}
