package com.jmp.wayback.common

import com.jmp.wayback.common.Either.Error
import com.jmp.wayback.common.Either.Success

sealed class Either<out L, out R> {

    data class Success<R>(val data: R) : Either<Nothing, R>()
    data class Error<L>(var failure: L) : Either<L, Nothing>()

    fun <L> left(a: L) = Error(a)
    fun <R> right(b: R) = Success(b)

    companion object {
        suspend fun <T> request(hasInternetConnectivity: Boolean, call: suspend () -> T): Either<Failure, T> {
            return try {
                if (!hasInternetConnectivity)
                    throw Failure.NoConnectivityException
                Success(call())
            } catch (exception: Failure.NoConnectivityException) {
                Error(Failure.NoConnectivity)
            } catch (exception: Exception) {
                Error(Failure.UnknownError)
            }
        }
    }
}

fun <L, R> Either<L, R>.doOnSuccess(function: (R) -> Any): Either<L, R> =
    when (this) {
        is Error -> Error(failure)
        is Success -> {
            function(data)
            Success(data)
        }
    }

fun <L, R> Either<L, R>.doOnError(function: (L) -> Any): Either<L, R> =
    when (this) {
        is Error -> {
            function(failure)
            Error(failure)
        }
        is Success -> {
            Success(data)
        }
    }

fun <R> Either<Failure, R>.getOrNull(): R? =
    when (this) {
        is Error -> null
        is Success -> data
    }
