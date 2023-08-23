package com.example.a2023hackathon

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ItemTaskModel(
    var docId: String? = null,
    var title: String? = null,
    var content: String? = null,
    var date: String? = null,
    var s_date: String? = null,
    var d_date: String? = null,
    var professor: String? = null,
    var major: String? = null,
    var sub_code: String? = null,
    var state: String? = "In progress",
)
