package com.nyangzzi.withconimal.presentation.util

import android.content.Context
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import java.util.Locale

object Utils {
    fun setImageUrl(url: String?) = Constant.convertImageUrl + url
    fun dateFormat(date: String?) =
        if (date != null && date.length == 8) "${date.substring(0, 4)}.${
            date.substring(
                4,
                6
            )
        }.${date.substring(6, 8)}" else null

    fun formatComma(number: Int) = String.format(Locale.KOREA, "%,d", number)
    suspend fun shareAnimal(context: Context, animalInfo: AnimalInfo) = shareImageAndText(
        context = context,
        imageFile = saveBitmapToFile(
            context = context,
            bitmap = loadImageFromUrl(context = context, imageUrl = animalInfo.popfile ?: ""),
            fileName = "${animalInfo.desertionNo}"
        ),
        text = getSharingAnimalInfoText(animalInfo)
    )
}