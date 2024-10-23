package com.nyangzzi.withconimal.domain.model.network

data class ResponseHeader (
    val reqNo: String? = null,
    val resultCode: String? = null,
    val resultMsg: String? = null,
    val errorMsg: String? = null
)