buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
//Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false

    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}
// task clean(type: Delete) {
//     delete rootProject.buildDir)
// }
 task<Delete>("clean") {
     delete(rootProject.buildDir)
 }




//task clean(type: Delete){
//    delete rootProject.buildDir
//}
//plugins {
//    id ("com.android.application") version "8.2.0" apply false
//    id ("com.android.library") version "8.2.0" apply false
//    id ("org.jetbrains.kotlin.android") version "1.9.10" apply false
//}
//
//task clean(type: Delete){
//   delete rootProject.buildDir

//plugins {
//    kotlin("android") version "1.9.10" apply false
//    kotlin("android-library") version "1.9.10" apply false
//
//}
//
//tasks {
//    register<Delete>("clean") {
//        delete(rootProject.buildDir)
//    }
//}
