package com.example.githubusersub.utils

sealed class MyResult {
    data class Success<out T>(val data: T) : MyResult()
    data class Error(val exception: Throwable) : MyResult()
    data class Loading(val isLoading: Boolean) : MyResult()
}