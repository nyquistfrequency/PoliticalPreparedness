package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.*
import com.example.android.politicalpreparedness.Constants.ELECTION_TABLE_NAME
import com.squareup.moshi.*
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = ELECTION_TABLE_NAME)
data class Election(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "electionDay")val electionDay: Date,
    @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division,
    @ColumnInfo(name = "isSaved") var isSaved: Boolean = false
) : Parcelable

