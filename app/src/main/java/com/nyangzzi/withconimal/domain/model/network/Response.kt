package com.nyangzzi.withconimal.domain.model.network

data class Response<T>(
    val header: ResponseHeader = ResponseHeader(),
    val body: ResponseBody<T>?
)