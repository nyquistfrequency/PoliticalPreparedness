package com.example.android.politicalpreparedness.repository

import android.annotation.SuppressLint
import android.net.Network
import android.util.Log
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(private val database: ElectionDatabase) {

    @SuppressLint("LogNotTimber")
    suspend fun refreshElections() {
        withContext(Dispatchers.IO){
            try {
                val electionList = CivicsApi.retrofitService.getElections()
                database.electionDao.insertAllElections(*electionList.elections.asDatabaseModel())
                Log.i("refreshElections", "Success")
            }
            catch(err: java.lang.Exception){
                Log.e("Failed refreshing Elections", err.message.toString())
            }
        }
    }
}