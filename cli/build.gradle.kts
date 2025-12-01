plugins {
    id("application")
    id("java")
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
    // SUA CLI INTERNA → “kc”
    // O usuário nunca vai rodar "k" dentro do jar.
    mainClass.set("org.klang.cli.KMain")
}

tasks {
    shadowJar {
        archiveBaseName.set("klang")   // nome final correto
        archiveClassifier.set("")       // sem -all
        archiveVersion.set("")          // sem versão no nome

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

    // Remove o .jar padrão
    jar {
        enabled = false
    }
}
