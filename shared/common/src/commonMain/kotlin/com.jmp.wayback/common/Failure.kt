package com.jmp.wayback.common

sealed class Failure {

    data object NoConnectivityException : Exception()

    data object NoConnectivity : Failure()

    data object UnknownError : Failure()

    data object ErrorDisablingOnboarding : Failure()

    data object LocationNotFound : Failure()

    data object ErrorSavingParkingInformation : Failure()
}
