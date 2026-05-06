package com.back.global.extensions

fun <T: Any> T?.getOrThrow(): T {
    return this ?: throw NoSuchElementException()
}