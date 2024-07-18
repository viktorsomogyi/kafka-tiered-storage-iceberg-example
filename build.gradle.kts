plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    implementation("com.opencsv:opencsv:5.9")
    implementation("org.apache.kafka:kafka-clients:3.7.1")
    implementation("org.apache.spark:spark-sql_2.12:3.5.1")
    implementation("org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.1")
    implementation("org.apache.spark:spark-streaming_2.12:3.5.1")
    implementation("org.apache.spark:spark-streaming-kafka-0-10_2.12:3.5.1")
    implementation("org.apache.iceberg:iceberg-spark3-runtime:0.13.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "AggregateInSparkKt.main"
    }
    isZip64 = true
    from(sourceSets.main.get().output)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}