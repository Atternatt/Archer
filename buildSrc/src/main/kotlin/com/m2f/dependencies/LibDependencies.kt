package com.m2f.dependencies

infix fun String.version(version: String): String = "$this:$version"


private const val coroutinesVersion = "1.5.2"

//region Common
val coroutinesBucket = listOf(
    "org.jetbrains.kotlinx:kotlinx-coroutines-core" version coroutinesVersion
)

private const val arrowVersion = "1.0.0"
val functionalBucket = listOf(
    "io.arrow-kt:arrow-core" version arrowVersion
)
//endregion
