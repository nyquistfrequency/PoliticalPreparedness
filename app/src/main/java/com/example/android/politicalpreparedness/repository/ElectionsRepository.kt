package com.example.android.politicalpreparedness.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(private val electionDatabase: ElectionDatabase) {

    val upcomingElections = electionDatabase.electionDao.getAllElections()
    val savedElections = electionDatabase.electionDao.getSavedElections()

    @SuppressLint("LogNotTimber")
    suspend fun refreshElections() {
        withContext(Dispatchers.IO){
            try {
                val electionResponse = CivicsApi.retrofitService.getElections()
                electionDatabase.electionDao.insertAllElections(*electionResponse.elections.asDatabaseModel())
                Log.i("refreshElections", "Success")
            }
            catch(err: java.lang.Exception){
                Log.e("refreshElections", err.message.toString())
            }
        }
    }
    suspend fun refreshSavedElections() {
        withContext(Dispatchers.IO){
            try {
                electionDatabase.electionDao.getSavedElections()
                Log.i("refreshSavedElections", "Success")
            }
            catch(err: java.lang.Exception){
                Log.e("refreshElections", err.message.toString())
            }
        }
    }


//    suspend fun followElection(election: Election?) {
//        withContext(Dispatchers.IO) {
//            election?.let {
//                electionDatabase.electionDao.followElectionById(it.id)
//            }
//        }
//    }
//
//    suspend fun unfollowElection(election: Election?) {
//        withContext(Dispatchers.IO) {
//            election?.let {
//                electionDatabase.electionDao.unfollowElectionById(it.id)
//            }
//        }
//    }



    suspend fun getClickedElection(){

    }


}