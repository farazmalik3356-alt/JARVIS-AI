package com.jarvis.ai.data.remote

import com.jarvis.ai.core.JarvisError
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object HttpErrorMapper {
    fun map(throwable: Throwable): JarvisError = when (throwable) {
        is JarvisError -> throwable
        is SocketTimeoutException -> JarvisError.Timeout()
        is UnknownHostException -> JarvisError.Network(throwable)
        is IOException -> JarvisError.Network(throwable)
        else -> JarvisError.Unknown(throwable)
    }
}