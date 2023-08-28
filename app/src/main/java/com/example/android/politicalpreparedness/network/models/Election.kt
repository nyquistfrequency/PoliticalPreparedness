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
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division
) : Parcelable

// From DevBytes Code
fun List<Election>.asDomainModel(): List<Election> {
        return map {
                Election(
                        id = it.id,
                        name = it.name,
                        electionDay = it.electionDay,
                        division = it.division
                )
        }
}