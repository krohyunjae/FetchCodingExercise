package com.barleytea.networking.utils

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import io.reactivex.Single
import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import timber.log.Timber
import java.io.IOException

class ApiParseException(cause: Throwable) : RuntimeException(cause)

sealed class ApiResponse<out TContent : Any> {
    sealed class Success<out TContent : Any>(open val httpCode: Int) : ApiResponse<TContent>() {
        data class WithContent<out TContent : Any>(
            val content: TContent,
            override val httpCode: Int = HttpStatus.HTTP_OK
        ) : Success<TContent>(httpCode)

        data object NoContent : Success<Nothing>(httpCode = HttpStatus.HTTP_NO_CONTENT)
    }

    sealed class Error : ApiResponse<Nothing>() {
        data object NoInternet : Error()
        open class Response private constructor(
            val httpCode: Int,
            private val getErrorBodyAsType: (Class<*>) -> Any?
        ) : Error() {
            constructor(httpCode: Int, responseBody: ResponseBody, moshi: Moshi) :
                    this(httpCode, getTypedErrorFromMoshi(moshi, responseBody))

            constructor(httpCode: Int, errorBody: Any? = null) :
                    this(httpCode, { errorBody }) {
                require(errorBody !is ResponseBody) {
                    "errorBody should be the data class, not a ResponseBody"
                }
            }

            object Unauthorized : Response(httpCode = 401)

            object NotFound : Response(httpCode = 404)

            companion object {
                fun getTypedErrorFromMoshi(moshi: Moshi, responseBody: ResponseBody): (Class<*>) -> Any? {
                    return { clazz->
                        if (clazz == String::class.java) {
                            responseBody.string()
                        } else {
                            try {
                                moshi.adapter(clazz).fromJson(responseBody.source())
                            } catch (throwable: Throwable) {
                                null
                            }
                        }
                    }
                }
            }

            override fun equals(other:Any?): Boolean {
                return (other is Response) && other.httpCode == this.httpCode
            }

            override fun toString(): String {
                return "ApiResponse: Error: $httpCode"
            }

            override fun hashCode(): Int {
                var result = httpCode
                result = 31 * result + getErrorBodyAsType.hashCode()
                return result
            }
        }

        data class Unexpected(
            val throwable: Throwable?
        ) : Error() {
            override fun equals(other: Any?) : Boolean {
                if (other !is Unexpected) {
                    return false
                }
                if (other.throwable == null) {
                    return throwable == null
                } else if(throwable == null) {
                    return false
                }

                return other.throwable::class.java  == throwable::class.java &&
                        other.throwable.message == throwable.message
            }

            override fun hashCode(): Int {
                return throwable?.hashCode() ?: 0
            }
        }
    }
}

private fun <T : Any> Response<T>.mapResponse(moshi: Moshi? = null): ApiResponse<T> {
    val response = this
    return when {
        HttpStatus.isHttpSuccess(response.code()) -> {
            val body = response.body()
            if (body != null) {
                ApiResponse.Success.WithContent(content = body, httpCode = response.code())
            } else {
                ApiResponse.Success.NoContent
            }
        }

        else -> {
            val responseBody = response.errorBody()
            if (moshi != null && responseBody != null) {
                ApiResponse.Error.Response(httpCode =  response.code(), responseBody = responseBody, moshi = moshi)
            } else {
                ApiResponse.Error.Response(httpCode = response.code(), errorBody = null)
            }
        }
    }
}

suspend fun <T: Any> HasMoshi.toApiResponse(block: suspend () -> Response<T>): ApiResponse<T> {
    return runCatching { block().mapResponse(moshi) }.getOrElse { error -> mapApiException(error)}
}

fun <T: Any> mapApiException(error: Throwable): ApiResponse<T> {
    return when (error) {
        is JsonEncodingException -> {
            // Response is not JSON
            ApiResponse.Error.Unexpected(ApiParseException(error))
        }
        is JsonDataException -> {
            // Required fields missing
            ApiResponse.Error.Unexpected(ApiParseException(error))
        }

        is SerializationException -> {
            // Kotlin Serialization Exception
            ApiResponse.Error.Unexpected(ApiParseException(error))
        }
        is IOException -> {
            // IOException = Network Issue ≈ NoInternet
            ApiResponse.Error.NoInternet
        }

        else -> {
            // everything else is unexpected
            ApiResponse.Error.Unexpected(error)
        }
    }
}