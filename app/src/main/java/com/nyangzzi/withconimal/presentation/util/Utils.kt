package com.nyangzzi.withconimal.presentation.util

object Utils {
    fun setImageUrl(url: String?) = Constant.convertImageUrl + url
    fun dateFormat(date: String?) = if(date != null && date.length == 8) "${date.substring(0,4)}.${date.substring(4,6)}.${date.substring(6,8)}" else ""
}