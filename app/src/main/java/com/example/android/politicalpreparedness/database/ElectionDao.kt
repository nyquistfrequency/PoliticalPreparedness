package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    //TODO: Add insert query

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAllElections(vararg elections: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllElections(vararg elections: Election)
    @Insert
    suspend fun insertElection(election: Election)

    //TODO: Add select all election query
    @Query("SELECT * FROM ${Constants.ELECTION_TABLE_NAME}")
    fun getAllElections(): LiveData<List<Election>>

    //TODO: Add select single election query
    @Query("SELECT * FROM ${Constants.ELECTION_TABLE_NAME} where id = :id")
    suspend fun getElectionById(id: Int): Election?

    //TODO: Add delete query
    @Query("DELETE FROM ${Constants.ELECTION_TABLE_NAME} where id=:id")
    suspend fun deleteElectionById(id: Int)

    //TODO: Add clear query - TBD: Why do I need a clear query?
    @Query("DELETE FROM ${Constants.ELECTION_TABLE_NAME}")
    suspend fun clear()


}