package com.jmp.wayback.common

sealed class Failure(val message: Int) {

    data object NoConnectivityException : Exception()

    class NoConnectivity : Failure(0)

    class UnknownError : Failure(0)
}
