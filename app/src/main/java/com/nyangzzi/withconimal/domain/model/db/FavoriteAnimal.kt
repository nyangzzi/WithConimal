package com.nyangzzi.withconimal.domain.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @property desertionNo 유기번호
 * @property filename Thumbnail Image
 * @property happenDt 접수일
 * @property happenPlace 발견장소
 * @property kindCd 품종
 * @property colorCd 색상
 * @property age 나이
 * @property weight 체중
 * @property noticeNo 공고번호
 * @property noticeSdt 공고시작일
 * @property noticeEdt 공고종료일
 * @property popfile Image
 * @property processState 상태
 * @property sexCd 성별
 * @property neuterYn 중성화여부
 * @property specialMark 특징
 * @property careNm 보호소이름
 * @property careTel 보호소전화번호
 * @property careAddr 보호장소
 * @property orgNm 관할기관
 * @property chargeNm 담당자
 * @property officetel 담당자연락처
 * @property noticeComment 특이사항
 **/

@Entity(tableName = "favorite_animals")
data class FavoriteAnimal(
 @PrimaryKey(autoGenerate = true) val desertionNo: Long? = null,
 val filename: String? = null,
 val happenDt: String? = null,
 val happenPlace: String? = null,
 val kindCd: String? = null,
 val colorCd: String? = null,
 val age: String? = null,
 val weight: String? = null,
 val noticeNo: String? = null,
 val noticeSdt: String? = null,
 val noticeEdt: String? = null,
 val popfile: String? = null,
 val processState: String? = null,
 val sexCd: String? = null,
 val neuterYn: String? = null,
 val specialMark: String? = null,
 val careNm: String? = null,
 val careTel: String? = null,
 val careAddr: String? = null,
 val orgNm: String? = null,
 val chargeNm: String? = null,
 val officetel: String? = null,
 val noticeComment: String? = null,

 )