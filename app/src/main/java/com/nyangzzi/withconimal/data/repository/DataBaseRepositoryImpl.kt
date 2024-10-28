package com.nyangzzi.withconimal.data.repository

import com.nyangzzi.withconimal.data.dao.FavoriteAnimalDao
import com.nyangzzi.withconimal.data.dao.FavoriteAnimalDatabase
import com.nyangzzi.withconimal.domain.model.common.AnimalInfo
import com.nyangzzi.withconimal.domain.model.db.FavoriteAnimal
import com.nyangzzi.withconimal.domain.repository.DataBaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataBaseRepositoryImpl @Inject constructor(
    private val favoriteAnimalDao: FavoriteAnimalDao
) : DataBaseRepository {
    override suspend fun addAnimal(animal: AnimalInfo) {
        favoriteAnimalDao.insert(favoriteModelToEntity(animal))
    }

    override fun getAllAnimals(): Flow<List<AnimalInfo>> {
        return favoriteAnimalDao.getAllAnimals().map { favoriteList ->
            favoriteList.map {
                favoriteEntityToModel(it)
            }
        }
    }

    override suspend fun updateAnimal(animal: AnimalInfo) {
        favoriteAnimalDao.update(favoriteModelToEntity(animal))
    }

    override suspend fun deleteAnimal(animal: AnimalInfo) {
        favoriteAnimalDao.delete(favoriteModelToEntity(animal))
    }

    private fun favoriteModelToEntity(animalInfo: AnimalInfo) = FavoriteAnimal(
        desertionNo = animalInfo.desertionNo?.toLong(),
        filename = animalInfo.filename,
        happenDt = animalInfo.happenDt,
        happenPlace = animalInfo.happenPlace,
        kindCd = animalInfo.kindCd,
        colorCd = animalInfo.colorCd,
        age = animalInfo.age,
        weight = animalInfo.weight,
        noticeNo = animalInfo.noticeNo,
        noticeSdt = animalInfo.noticeSdt,
        noticeEdt = animalInfo.noticeEdt,
        popfile = animalInfo.popfile,
        processState = animalInfo.processState,
        sexCd = animalInfo.sexCd,
        neuterYn = animalInfo.neuterYn,
        specialMark = animalInfo.specialMark,
        careNm = animalInfo.careNm,
        careTel = animalInfo.careTel,
        careAddr = animalInfo.careAddr,
        orgNm = animalInfo.orgNm,
        chargeNm = animalInfo.chargeNm,
        officetel = animalInfo.officetel,
        noticeComment = animalInfo.noticeComment,
    )

    private fun favoriteEntityToModel(favoriteInfo: FavoriteAnimal) = AnimalInfo(
        desertionNo = favoriteInfo.desertionNo.toString(),
        filename = favoriteInfo.filename,
        happenDt = favoriteInfo.happenDt,
        happenPlace = favoriteInfo.happenPlace,
        kindCd = favoriteInfo.kindCd,
        colorCd = favoriteInfo.colorCd,
        age = favoriteInfo.age,
        weight = favoriteInfo.weight,
        noticeNo = favoriteInfo.noticeNo,
        noticeSdt = favoriteInfo.noticeSdt,
        noticeEdt = favoriteInfo.noticeEdt,
        popfile = favoriteInfo.popfile,
        processState = favoriteInfo.processState,
        sexCd = favoriteInfo.sexCd,
        neuterYn = favoriteInfo.neuterYn,
        specialMark = favoriteInfo.specialMark,
        careNm = favoriteInfo.careNm,
        careTel = favoriteInfo.careTel,
        careAddr = favoriteInfo.careAddr,
        orgNm = favoriteInfo.orgNm,
        chargeNm = favoriteInfo.chargeNm,
        officetel = favoriteInfo.officetel,
        noticeComment = favoriteInfo.noticeComment,
    )
}

