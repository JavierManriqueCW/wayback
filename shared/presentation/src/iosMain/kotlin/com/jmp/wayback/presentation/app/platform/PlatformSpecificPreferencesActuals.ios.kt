package com.jmp.wayback.presentation.app.platform

import com.jmp.wayback.presentation.updateWidget

actual fun updatePlatformSpecificIsParkedStatus(isParked: Boolean) {
    updateWidget?.invoke(isParked)
}
