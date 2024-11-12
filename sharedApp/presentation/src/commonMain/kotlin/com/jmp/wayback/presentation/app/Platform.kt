package com.jmp.wayback.presentation.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform