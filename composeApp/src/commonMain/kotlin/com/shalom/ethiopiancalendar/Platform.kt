package com.shalom.ethiopiancalendar

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform