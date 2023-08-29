package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.network.models.VoterInfo

@Dao
interface VoterInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVoterInfo(voterInfo: VoterInfo)

    @Query("SELECT * FROM ${Constants.VOTER_INFO_TABLE_NAME} where id = :id")
    suspend fun getVoterInfoById(id: Int): VoterInfo?

}