package com.iceye.utils

fun getRandom(from: Long = 0, to: Long = 1_000_000): Long {
    return (from..to).random()
}