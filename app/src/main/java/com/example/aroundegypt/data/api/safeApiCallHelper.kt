package com.example.aroundegypt.data.api

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


suspend fun <T> safeApiCallHelper(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): Result<T> {

    return withContext(dispatcher) {
        try {

            Result.Success(apiCall.invoke())

        } catch (throwable: Throwable) {
            Log.e("safeApiCall", throwable.localizedMessage?.toString() ?: "")
            when (throwable) {
                is IOException -> {
                    Result.Error("NetworkError")
                }

                is HttpException -> {
                    //  val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    Result.Error(errorResponse?.message ?: "")
                }

                is KotlinNullPointerException -> {
                    Result.Error("NoContentError")
                }

                else -> {
                    Result.Error(null)
                }
            }
        }
    }

}

private fun convertErrorBody(throwable: HttpException): ErrorObject? {
    val s = throwable.response()?.errorBody()?.string()
    Log.e("convertErrorBody", s ?: "")

    return if (s != null) {
        ErrorObject(s)
    } else {
        null
    }

}

data class ErrorObject(
    val message:String)