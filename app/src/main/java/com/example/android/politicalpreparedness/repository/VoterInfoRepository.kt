package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.VoterInfoDatabase
import com.example.android.politicalpreparedness.network.models.VoterInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VoterInfoRepository(private val voterInfoDatabase: VoterInfoDatabase, private val electionDatabase: ElectionDatabase) {

private val voterInfoForSelectedElection = MutableLiveData<VoterInfo>()

    suspend fun followElection(election: Int) {
        withContext(Dispatchers.IO) {
            election.let {
                electionDatabase.electionDao.followElectionById(it)
            }
        }
    }

    suspend fun unfollowElection(election: Int) {
        withContext(Dispatchers.IO) {
            election.let {
                electionDatabase.electionDao.unfollowElectionById(it)
            }
        }
    }


    suspend fun getElectionSavedStatus(electionId : Int): Boolean {
        return withContext(Dispatchers.IO) {
            val election = electionDatabase.electionDao.getElectionById(electionId)
            election?.isSaved ?: false // Return the saved status if the election is found, otherwise return false
        }
    }
    suspend fun refreshVoterInfo(address: Int, id: String) {
//        withContext(Dispatchers.IO) {
//            try {
//                val voterInfoResponse = CivicsApi.retrofitService.getVoterInfo(address, id)
//                val voterInfo = voterInfoResponse.asDatabaseModel()
//                database.voterInfoDao.insertVoterInfo(voterInfo)
//
//                // voterInfoForSelectedElection.postValue(voterInfo)
//
//                Log.i("refreshVoterInfo", "Success")
//            } catch (err: java.lang.Exception) {
//                Log.e("refreshVoterInfo", err.message.toString())
//            }
//
//        }
    }

//    suspend fun refreshVoterInfo(electionId: Int, address: String) {
//
//        withContext(Dispatchers.IO) {
//
//            try {
//                val networkVoterInfoResponse = client.getVoterInfo(electionId, address)
//
//                val voterInfoDomainModel = networkVoterInfoResponse.asDomainModel()
//
//                voterInfoForSelectedElection.postValue(voterInfoDomainModel)
//
//
//            } catch (e: Exception) {
//
//                Log.d("ExceptionInRefreshVoterInfo", e.toString())
//            }
//
//        }
//
//    }

    suspend fun refreshButtonState() {
        withContext(Dispatchers.IO) {
            try {

                Log.i("refreshButtonState", "Success")
            } catch (err: java.lang.Exception) {
                Log.e("refreshButtonState", err.message.toString())
            }

        }
    }


}