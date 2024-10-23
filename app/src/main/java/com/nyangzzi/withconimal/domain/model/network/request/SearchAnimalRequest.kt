package com.nyangzzi.withconimal.domain.model.network.request

/**
 *
 * @property bgnde 유기날짜 (검색 시작일)
 * @property endde 유기날짜 (검색 종료일)
 * @property upkind 축종코드
 * @property kind 품종코드
 * @property uprCd 시도코드
 * @property orgCd 시군구코드
 * @property careRegNo 보호소 번호
 * @property state 상태
 * @property neuterYn 중성화여부
 */
data class SearchAnimalRequest (
    val bgnde: String? = null,
    val endde: String? = null,
    val upkind: String? = null,
    val kind: String? = null,
    val uprCd: String? = null,
    val orgCd: String? = null,
    val careRegNo: String? = null,
    val state: String? = null,
    val neuterYn: String? = null,
    )