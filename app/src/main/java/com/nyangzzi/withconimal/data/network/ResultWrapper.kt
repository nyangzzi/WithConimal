package com.nyangzzi.withconimal.data.network

sealed class ResultWrapper<out R> {

    object None : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val errorMessage: String) : ResultWrapper<Nothing>()

    override fun toString(): String {
        return when (this) {
            is None -> "None"
            is Loading -> "loading"
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[errorMessage=$errorMessage]"
        }
    }
}