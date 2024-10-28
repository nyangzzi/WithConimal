package com.nyangzzi.withconimal.presentation.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.common.NeuterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

fun shareImageAndText(context: Context, imageFile: File?, text: String) {
    if(imageFile == null){
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, "[윗코니멀]"))
    }
    else {
        val uri =
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)       // 텍스트 추가
            putExtra(Intent.EXTRA_STREAM, uri)      // 이미지 URI 추가
            type = "image/*"                        // 타입 설정
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "[윗코니멀]"))
    }
}

suspend fun loadImageFromUrl(context: Context, imageUrl: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .build()

        val result = (loader.execute(request) as? SuccessResult)?.drawable
        (result as? BitmapDrawable)?.bitmap
    }
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap?, fileName: String): File? {

    if(bitmap == null) return null

    val file = File(context.cacheDir, "$fileName.png") // 캐시 디렉토리에 저장
    FileOutputStream(file).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
    }
    return file
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