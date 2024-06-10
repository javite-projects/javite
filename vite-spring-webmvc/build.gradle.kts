import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SonatypeHost

object Meta {
    const val RELEASE = "https://s01.oss.sonatype.org/service/local/"
    const val SNAPSHOT = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
    const val DESC = "Vite integration with Spring Web MVC."
    const val LICENSE = "MIT"
    const val LICENSE_URL = "https://opensource.org/licenses/mit"
    const val GITHUB_REPO = "benny123tw/vite-integration"
    const val DEVELOPER_ID = "benny123tw"
    const val DEVELOPER_NAME = "Benny Yen"
    const val DEVELOPER_ORGANIZATION = "io.github.benny123tw"
    const val DEVELOPER_ORGANIZATION_URL = "https://www.github.com/benny123tw"
}

plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.publish.maven)
}

description = "Vite - Spring Web MVC"

dependencies {
    implementation(libs.spring.core)
    implementation(libs.spring.webmvc)
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.jackson.databind)
    implementation(libs.jakarta.servlet)
    implementation(libs.jakarta.servlet.jsp)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.junit)
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
    archiveClassifier.set("")
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
            ),
        )
    }
}

mavenPublishing {
    println("Publish ${project.group}, ${project.name}, ${project.version}")
    coordinates(project.group.toString(), project.name, project.version.toString())

    // Correctly configure JavaLibrary with Javadoc and sources JARs
    configure(
        JavaLibrary(
            javadocJar = JavadocJar.Javadoc(),
            sourcesJar = true
        )
    )

    pom {
        name.set(project.name)
        description.set(Meta.DESC)
        inceptionYear.set("2024")
        url.set(Meta.GITHUB_REPO)
        licenses {
            license {
                name.set(Meta.LICENSE)
                url.set(Meta.LICENSE_URL)
            }
        }
        developers {
            developer {
                id.set(Meta.DEVELOPER_ID)
                name.set(Meta.DEVELOPER_NAME)
                organization.set(Meta.DEVELOPER_ORGANIZATION)
                organizationUrl.set(Meta.DEVELOPER_ORGANIZATION_URL)
            }
        }
        scm {
            url.set("https://github.com/${Meta.GITHUB_REPO}.git")
            connection.set("scm:git:git://github.com/${Meta.GITHUB_REPO}.git")
            developerConnection.set("scm:git:git://github.com/${Meta.GITHUB_REPO}.git")
        }
        issueManagement {
            url.set("https://github.com/${Meta.GITHUB_REPO}/issues")
        }
    }
}

// Ensure that generateMetadataFileForMavenPublication depends on the javadocJar task
//tasks.named("generateMetadataFileForMavenPublication") {
//    dependsOn(tasks.named("javadocJar"))
//}

// gradle locking of dependency versions
//   *required+used for trivy scan
dependencyLocking {
    lockAllConfigurations()
}

// always run subproject task with parent
rootProject.tasks.dependencies { dependsOn(tasks.dependencies) }