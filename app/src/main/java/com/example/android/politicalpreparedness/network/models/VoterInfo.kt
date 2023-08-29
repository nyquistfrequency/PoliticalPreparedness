package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Constants.VOTER_INFO_TABLE_NAME)
data class VoterInfo(
    @PrimaryKey val id: Int,
    val votingLocations: String?,
    val ballotInformation: String?)
    : Parcelable

