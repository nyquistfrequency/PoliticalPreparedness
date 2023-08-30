package com.example.android.politicalpreparedness.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.VoterInfoDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.VoterInfo
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VoterInfoRepository(
    private val voterInfoDatabase: VoterInfoDatabase,
    private val electionDatabase: ElectionDatabase
) {

    private val _voterInfo = MutableLiveData<VoterInfo>()
    val voterInfo: LiveData<VoterInfo>
        get() = _voterInfo

    fun getElectionById(election: Int) = electionDatabase.electionDao.getElectionById(election)

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

    // Created a second DB for VoterInfo
    @SuppressLint("LogNotTimber")
    suspend fun refreshVoterInfoResponse(address:String, id:Int) {
        withContext(Dispatchers.IO) {
            try {
                val voterInfoResponse = CivicsApi.retrofitService.getVoterInfo(address, id)
                Log.i("voterInfoResponse", "Successfully received VoterInfo from API")
                val voterInfoResponseToVoterInfo = convertToVoterInfo(id, voterInfoResponse)
                voterInfoResponseToVoterInfo?.run {
                    voterInfoDatabase.voterInfoDao.insertVoterInfo(this)
                }
            }
            catch (err: java.lang.Exception){
                Log.e("voterInfoResponse", err.message.toString())
            }
        }
    }

    suspend fun loadVoterInfoById(id: Int) {
        withContext(Dispatchers.IO) {
            val data = voterInfoDatabase.voterInfoDao.getVoterInfoById(id)
            data?.run {_voterInfo.postValue(this)}
        }
    }

    private fun convertToVoterInfo(id: Int, voterInfoResponse: VoterInfoResponse) : VoterInfo? {

        var voterInfo: VoterInfo? = null
        val state = voterInfoResponse.state

        if (state?.isNotEmpty() == true) {
            val votingLocationUrl: String? =
                state[0].electionAdministrationBody.votingLocationFinderUrl?.run {
                    this
                }

            val ballotInfoUrl: String? =
                state[0].electionAdministrationBody.ballotInfoUrl?.run {
                    this
                }

            voterInfo = VoterInfo(
                id,
                state[0].name,
                votingLocationUrl,
                ballotInfoUrl
            )
        }

        return voterInfo
    }


}