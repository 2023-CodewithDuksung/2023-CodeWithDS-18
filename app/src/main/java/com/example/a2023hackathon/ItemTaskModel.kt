package com.example.a2023hackathon

data class ItemTaskModel(
    var docId: String? = null,
    var title: String? = null, // 과제명
    var content: String? = null, // 과제 내용
    var date: String? = null, // 등록 날짜
    var s_date: String? = null, // 시작일
    var d_date: String? = null, // 마감일
    var professor: String? = null, // 교수명
    var major: String? = null // 학과명
)

