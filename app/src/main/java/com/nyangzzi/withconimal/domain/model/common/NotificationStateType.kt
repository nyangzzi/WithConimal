package com.nyangzzi.withconimal.domain.model.common

enum class NotificationStateType(val code: String?, val text: String) {
    ALL(code = null, text = "전체"),
    YES(code = "notice", text = "공고중"),
    NO(code = "protect", text = "보호중"),
}