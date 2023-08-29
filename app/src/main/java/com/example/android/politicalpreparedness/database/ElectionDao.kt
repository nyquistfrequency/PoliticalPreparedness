package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllElections(vararg elections: Election)

    @Insert
    suspend fun insertElection(election: Election)



    //TODO: Add select all election query
    @Query("SELECT * FROM ${Constants.ELECTION_TABLE_NAME}")
    fun getAllElections(): LiveData<List<Election>>

    @Query("SELECT * FROM ${Constants.ELECTION_TABLE_NAME} WHERE isSaved = 1")
    fun getSavedElections(): LiveData<List<Election>>

    //TODO: Add select single election query
    @Query("SELECT * FROM ${Constants.ELECTION_TABLE_NAME} WHERE id = :id")
    suspend fun getElectionById(id: Int): Election?

    @Query("UPDATE ${Constants.ELECTION_TABLE_NAME} SET isSaved = 1 WHERE id = :id")
    suspend fun followElectionById(id: Int)

    @Query("UPDATE ${Constants.ELECTION_TABLE_NAME} SET isSaved = 0 WHERE id=:id")
    suspend fun unfollowElectionById(id: Int)

    //TODO: Add delete query
    @Query("DELETE FROM ${Constants.ELECTION_TABLE_NAME} WHERE id=:id")
    suspend fun deleteElectionById(id: Int)

    //TODO: Add clear query - TBD: Why do I need a clear query?
    @Query("DELETE FROM ${Constants.ELECTION_TABLE_NAME}")
    suspend fun clear()


}