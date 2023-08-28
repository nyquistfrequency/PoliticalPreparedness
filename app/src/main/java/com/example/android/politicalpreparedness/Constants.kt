package com.example.android.politicalpreparedness

import java.text.SimpleDateFormat
import java.util.*

object Constants {
    //ToDo: Remove API_KEY before submission (to "DEMO_KEY")
    const val API_KEY = "AIzaSyCtIG0WG0yw8zWCKHnHbJ7jiok6LKZGjU8"
    const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"
    const val ELECTION_TABLE_NAME = "election_table"
    val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
}