plugins {
    java
    kotlin("jvm") version "1.5.10"
    id("me.champeau.jmh") version "0.6.5"
}

group = "io.cimi"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.openjdk.jmc:common:8.0.0-SNAPSHOT")
    implementation("org.openjdk.jmc:flightrecorder:8.0.0-SNAPSHOT")

    implementation("com.alibaba:fastjson:1.2.76")

    jmh("org.openjdk.jmc:common:8.0.0-SNAPSHOT")
    jmh("org.openjdk.jmc:flightrecorder:8.0.0-SNAPSHOT")

    testImplementation("junit:junit:4.13")
}

tasks.withType<Test> {
    maxHeapSize = "4g"
}
