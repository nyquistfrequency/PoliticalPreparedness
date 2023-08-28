package com.example.android.politicalpreparedness.network.jsonadapter

import com.example.android.politicalpreparedness.Constants.DATE_FORMAT
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

class DateAdapter {
    @FromJson
    fun dateFromJson(dateString: String): Date? {
        return DATE_FORMAT.parse(dateString)
    }

    @ToJson
    fun dateToJson(date: Date): String {
        return DATE_FORMAT.format(date)
    }
}