package com.nameisjayant.notecomposeapp.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

sealed class ResultState<out T> {

    data class Success<out R>(val data: R) : ResultState<R>()
    data class Failure(val msg: Throwable) : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()

}

fun <T> Flow<ResultState<T>>.doOnSuccess(action: suspend (T) -> Unit): Flow<ResultState<T>> =
    transform { result ->
        if (result is ResultState.Success) {
            action(result.data)
        }
        return@transform emit(result)
    }

fun <T> Flow<ResultState<T>>.doOnFailure(action: suspend (Throwable?) -> Unit): Flow<ResultState<T>> =
    transform { result ->
        if (result is ResultState.Failure) {
            action(result.msg)
        }
        return@transform emit(result)
    }

fun <T> Flow<ResultState<T>>.doOnLoading(action: suspend () -> Unit): Flow<ResultState<T>> =
    transform { result ->
        if (result is ResultState.Loading) {
            action()
        }
        return@transform emit(result)
    }