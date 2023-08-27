package com.example.android.politicalpreparedness.representative.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.network.models.Office
import com.example.android.politicalpreparedness.network.models.Official

//ToDo: Rework this structure - Needs to be similar to Election
@Entity
data class Representative (
        @PrimaryKey val id: Int,
        val official: Official,
        val office: Office
)