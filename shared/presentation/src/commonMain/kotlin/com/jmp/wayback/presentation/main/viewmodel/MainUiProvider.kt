package com.jmp.wayback.presentation.main.viewmodel

import wayback.shared.presentation.generated.resources.Res
import wayback.shared.presentation.generated.resources.main_background
import wayback.shared.presentation.generated.resources.main_detail_placeholder
import wayback.shared.presentation.generated.resources.park_button

class MainUiProvider {

    fun provide(): MainUiState =
        MainUiState(
            background = Res.drawable.main_background,
            detailPlaceholderText = Res.string.main_detail_placeholder,
            detailText = "",
            parkButtonText = Res.string.park_button
        )
}
