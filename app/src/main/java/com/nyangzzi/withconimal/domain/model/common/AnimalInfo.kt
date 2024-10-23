package com.nyangzzi.withconimal.domain.model.common

/**
 * @param desertionNo 유기번호
 * @param filename Thumbnail Image
 * @param happenDt 접수일
 * @param happenPlace 발견장소
 * @param kindCd 품종
 * @param colorCd 색상
 * @param age 나이
 * @param weight 체중
 * @param noticeNo 공고번호
 * @param noticeSdt 공고시작일
 * @param noticeEdt 공고종료일
 * @param popfile Image
 * @param processState 상태
 * @param sexCd 성별
 * @param neuterYn 중성화여부
 * @param specialMark 특징
 * @param careNm 보호소이름
 * @param careTel 보호소전화번호
 * @param careAddr 보호장소
 * @param orgNm 관할기관
 * @param chargeNm 담당자
 * @param officetel 담당자연락처
 * @param noticeComment 특이사항
 **/
data class AnimalInfo(
 val desertionNo: String? = null,
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