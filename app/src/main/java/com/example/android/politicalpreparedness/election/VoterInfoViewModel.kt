package com.example.android.politicalpreparedness.election

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.VoterInfoDatabase
import com.example.android.politicalpreparedness.repository.VoterInfoRepository
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    application: Application,
    private val electionId: Int
) : AndroidViewModel(application) {

    private val voterInfoDatabase = VoterInfoDatabase.getInstance(application)
    private val electionDatabase = ElectionDatabase.getInstance(application)
    private val voterInfoRepository = VoterInfoRepository(voterInfoDatabase, electionDatabase)


    private val _followButtonText = MutableLiveData<String>()
    val followButtonText: LiveData<String>
        get() = _followButtonText

    private val isElectionSaved: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        viewModelScope.launch {
            value = voterInfoRepository.getElectionSavedStatus(electionId)
        }
    }

    init {
        updateFollowButtonText()
    }

    fun onElectionInformationClicked() {
        //_navigateToVoterInfoFragment.value = election
    }

    fun doneNavigating() {
        // _navigateToVoterInfoFragment.value = null
    }


    fun toggleElectionSavedStatus() {
        viewModelScope.launch {
            val currentSavedStatus = voterInfoRepository.getElectionSavedStatus(electionId)
            val newSavedStatus = !currentSavedStatus
            // Save or remove the election from the local database based on the new saved status
            if (newSavedStatus) {
                // Save the election
                voterInfoRepository.followElection(electionId)
                _followButtonText.value = "Unfollow Election"
            } else {
                // Remove the election
                voterInfoRepository.unfollowElection(electionId)
                _followButtonText.value = "Follow Election"
            }
        }
    }

    fun updateFollowButtonText() {
        viewModelScope.launch {
            val currentSavedStatus = isElectionSaved.value ?: false
            _followButtonText.value = if (currentSavedStatus) {
                "Unfollow Election"
            } else {
                "Follow Election"
            }
        }
    }

@SuppressLint("LogNotTimber")
fun crosscheckIfIdPassedThrough() {
    Log.i("electionId_of_Element_picked", electionId.toString())
}
}

//TODO: Add live data to hold voter info

//TODO: Add var and methods to populate voter info

//TODO: Add var and methods to support loading URLs

//TODO: Add var and methods to save and remove elections to local database
//TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

/**
 * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
 */

