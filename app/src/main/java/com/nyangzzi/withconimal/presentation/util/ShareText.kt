package com.nyangzzi.withconimal.presentation.util

import android.content.Context
import android.content.Intent
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.common.NeuterType

fun shareText(context: Context, text: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "[윗코니멀]"))
}

fun getSharingAnimalInfoText(animalInfo: AnimalInfo) = "" +
        "${animalInfo.kindCd?.replace("개", "강아지")}\n" +
        "${animalInfo.specialMark}\n" +
        "\n" +
        "[특징]\n" +
        "색상: ${animalInfo.colorCd}\n" +
        "나이: ${animalInfo.age}\n" +
        "체중: ${animalInfo.weight}\n" +
        "성별: ${
            when (animalInfo.sexCd) {
                "M" -> "남"
                "F" -> "여"
                else -> "알 수 없음"
            }
        }\n" +
        "중성화: ${NeuterType.entries.firstOrNull { it.code == animalInfo.neuterYn }?.text}\n" +
        "\n" +
        "[보호소]\n" +
        "이름: ${animalInfo.careNm}\n"+
        "전화번호: ${animalInfo.careTel}\n"+
        "보호 장소: ${animalInfo.careAddr}\n"+
        "관할 기관: ${animalInfo.orgNm}\n"+
        "\n" +
        "[담당자]\n" +
        "이름: ${animalInfo.chargeNm}\n"+
        "연락처: ${animalInfo.officetel}\n"+
        "\n" +
        "[입양 공고]\n" +
        "공고 번호: ${animalInfo.noticeNo}\n"+
        "공고 일시: ${animalInfo.noticeSdt} ~ ${animalInfo.noticeEdt}\n"+
        "공고 상태: ${animalInfo.processState}\n"+
        "\n" +
        "[유기 정보]\n" +
        "유기 번호: ${animalInfo.desertionNo}\n"+
        "접수일: ${animalInfo.happenDt}\n"+
        "발견 장소: ${animalInfo.happenPlace}\n"+
        "\n" +
        "윗코니멀에서 더 많은 반려 동물들을 만나보세요!"