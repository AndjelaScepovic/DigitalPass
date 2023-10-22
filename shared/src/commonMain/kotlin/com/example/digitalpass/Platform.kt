package com.example.digitalpass

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform