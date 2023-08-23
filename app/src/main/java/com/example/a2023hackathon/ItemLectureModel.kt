package com.example.a2023hackathon

data class ItemLectureModel(
    var docId: String? = null,
    var name: String? = null,
    var term: String? = null,
    var professor: String? = null,
    var major: String? = null,
    var sub_code: String? = null,
    var mytasks: List<ItemTaskModel>? = null
)
