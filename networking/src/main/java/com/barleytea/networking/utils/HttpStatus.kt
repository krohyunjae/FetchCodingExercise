package com.barleytea.networking.utils

class HttpStatus {
    companion object {
        fun isHttpInfo(httpStatus: Int): Boolean {
            return (httpStatus / 100) == 1
        }

        fun isHttpSuccess(httpStatus: Int): Boolean {
            return (httpStatus / 100) == 2
        }

        const val HTTP_OK = 200
        const val HTTP_CREATED = 201
        const val HTTP_ACCEPTED = 202
        const val HTTP_NO_CONTENT = 204
        const val HTTP_RESET_CONTENT = 205

        fun isHttpRedirect(httpStatus: Int): Boolean {
            return (httpStatus / 100) == 3
        }
        const val HTTP_NOT_MODIFIED = 304

        fun isHttpClientError(httpStatus: Int): Boolean {
            return (httpStatus / 100) == 4
        }

        const val HTTP_BAD_REQUEST = 400
        const val HTTP_UNAUTHORIZED = 401
        const val HTTP_FORBIDDEN = 403
        const val HTTP_NOT_FOUND = 404

        fun isHttpServerError(httpStatus: Int): Boolean {
            return (httpStatus / 100) == 5
        }
        const val HTTP_INTERNAL_SERVER_ERROR = 500
    }
}