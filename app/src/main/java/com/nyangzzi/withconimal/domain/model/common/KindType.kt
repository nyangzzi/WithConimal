package com.nyangzzi.withconimal.domain.model.common

enum class KindType(val code: String?, val text: String) {
    ALL(code = null, text = "전체"),
    DOG(code = "417000", text = "강아지"),
    CAT(code = "422400", text = "고양이"),
    ETC(code = "429900", text = "기타"),
}