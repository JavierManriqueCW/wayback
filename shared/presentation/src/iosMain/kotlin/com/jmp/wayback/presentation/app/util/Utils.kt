package com.jmp.wayback.presentation.app.util

import platform.Foundation.NSBundle

fun String.localized(): String =
    NSBundle.mainBundle.localizedStringForKey(key = this, value = null, table = null)
