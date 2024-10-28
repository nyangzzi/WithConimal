package com.nyangzzi.withconimal.domain.model.network

data class ResponseBody<T> (
    val items: ResponseItems<T>,
    val numOfRows: Int? = null,
    val pageNo: Int? = null,
    val totalCount: Int? = null
)