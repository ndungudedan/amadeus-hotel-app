package com.amadeus.hotel

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform