package com.example.android.politicalpreparedness.election

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.VoterInfoDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.VoterInfoRepository
import kotlinx.coroutines.launch
import java.util.*

class VoterInfoViewModel(
    application: Application,
    election: Election
) : AndroidViewModel(application) {

    private val voterInfoDatabase = VoterInfoDatabase.getInstance(application)
    private val electionDatabase = ElectionDatabase.getInstance(application)
    private val voterInfoRepository = VoterInfoRepository(voterInfoDatabase, electionDatabase)

    private val electionId = election.id

    val voterInfo = voterInfoRepository.voterInfo

    val isElectionSaved: LiveData<Boolean> =
        voterInfoRepository.getElectionById(electionId).map { election ->
            election?.isSaved ?: false
        }

    val electionName: LiveData<String> =
        voterInfoRepository.getElectionById(electionId).map { election ->
            election?.name ?: ""
        }

    val electionDay: LiveData<Date> =
        voterInfoRepository.getElectionById(electionId).map { election ->
            election?.electionDay!!
        }

    init {
        refreshVoterInfo(election)
    }

    fun toggleElectionSavedStatus() {
        viewModelScope.launch {
            val currentSavedStatus = isElectionSaved.value
            val newSavedStatus = !currentSavedStatus!!
            // Save or remove the election from the local database based on the new saved status
            if (newSavedStatus) {
                // Save the election
                voterInfoRepository.followElection(electionId)
            } else {
                // Remove the election
                voterInfoRepository.unfollowElection(electionId)
            }
        }
    }

    @SuppressLint("LogNotTimber")
    private fun refreshVoterInfo(election: Election) {
        viewModelScope.launch {
            try {
                val state = election.division.state
                val address = "${state},${election.division.country}"

                Log.i("refreshVoterInfo state Value", state)
                Log.i("refreshVoterInfo address value", address)
                Log.i("refreshVoterInfo id value", electionId.toString())


                voterInfoRepository.refreshVoterInfoResponse(address, electionId)
                voterInfoRepository.loadVoterInfoById(electionId)

            } catch (err: java.lang.Exception) {
                Log.e("refreshVoterInfo", err.message.toString())
            }
        }
    }
}

