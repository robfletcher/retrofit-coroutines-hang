import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.3.31"
}

repositories {
  jcenter()
  maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.3.31"))
  implementation(kotlin("stdlib-jdk8"))

  testImplementation(platform("org.junit:junit-bom:5.4.2"))
  testImplementation("com.squareup.retrofit2:retrofit:2.6.0-SNAPSHOT")
  testImplementation("com.squareup.retrofit2:converter-jackson:2.6.0-SNAPSHOT")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")
  testImplementation("org.junit.jupiter:junit-jupiter-api")

  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}
