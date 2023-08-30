package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllElections(vararg elections: Election)

    @Query("SELECT * FROM ${Constants.ELECTION_TABLE_NAME}")
    fun getAllElections(): LiveData<List<Election>>

    @Query("SELECT * FROM ${Constants.ELECTION_TABLE_NAME} WHERE isSaved = 1")
    fun getSavedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM ${Constants.ELECTION_TABLE_NAME} WHERE id = :id")
    fun getElectionById(id: Int): LiveData<Election?>

    @Query("UPDATE ${Constants.ELECTION_TABLE_NAME} SET isSaved = 1 WHERE id = :id")
    suspend fun followElectionById(id: Int)

    @Query("UPDATE ${Constants.ELECTION_TABLE_NAME} SET isSaved = 0 WHERE id = :id")
    suspend fun unfollowElectionById(id: Int)

}