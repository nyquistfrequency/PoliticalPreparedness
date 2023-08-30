package com.example.android.politicalpreparedness.network.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.Constants

@Entity(tableName = Constants.VOTER_INFO_TABLE_NAME)
data class VoterInfo(
    @PrimaryKey val id: Int,
    val stateName: String,
    val votingLocation: String?,
    val ballotInformation: String?)

