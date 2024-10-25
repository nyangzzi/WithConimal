package com.nyangzzi.withconimal.domain.model.common

enum class NeuterType(val code: String?, val text: String) {
    ALL(code = null, text = "전체"),
    YES(code = "Y", text = "완료"),
    NO(code = "N", text = "미완료"),
    DONT(code = "U", text = "알 수 없음")
}