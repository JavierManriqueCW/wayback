package com.jmp.wayback.common

sealed class Failure(val message: Int? = null) {

    data object NoConnectivityException : Exception()

    class NoConnectivity : Failure()

    class UnknownError : Failure()

    class ErrorDisablingOnboarding : Failure()
}
