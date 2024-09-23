plugins {
    kotlin("jvm")
    //`embedded-kotlin`
    alias(libs.plugins.serialization)
}

//kotlin {
//    jvmToolchain(11)
//}
//
//java {
//    sourceCompatibility = JavaVersion.VERSION_11
//    targetCompatibility = JavaVersion.VERSION_11
//}

kotlin {

    dependencies{
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.client.auth)
        implementation(libs.ktor.client.cio)

        implementation(project(":core"))
    }

}