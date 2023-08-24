package com.example.a2023hackathon

import java.util.Date

data class ItemTaskModel(
    var docId: String? = null,
    var name: String? = null,
    var title: String? = null,
    var content: String? = null,
    var date: String? = null,
    var s_date: String? = null,
    var d_date: String? = null,
    var d_time: String? = null,
    var professor: String? = null,
    var major: String? = null,
    var sub_code: String? = null,
    var state: String? = "1",
    var isChecked: Boolean = false
)

