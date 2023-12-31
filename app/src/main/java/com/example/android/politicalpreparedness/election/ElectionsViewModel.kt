package com.example.android.politicalpreparedness.election

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.*

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = getInstance(application)
    private val electionsRepository = ElectionsRepository(database)

    val listOfUpcomingElections = electionsRepository.upcomingElections
    val listOfSavedElections = electionsRepository.savedElections

    init {
        viewModelScope.launch {
            electionsRepository.refreshElections()
            electionsRepository.refreshSavedElections()
        }
    }

    private val _navigateToVoterInfoFragment = MutableLiveData<Election?>()
    val navigateToVoterInfoFragment: LiveData<Election?>
        get() = _navigateToVoterInfoFragment

    fun onUpcomingElectionClicked(election: Election) {
        _navigateToVoterInfoFragment.value = election
    }

    fun onSavedElectionClicked(election: Election){
        _navigateToVoterInfoFragment.value = election
    }

    fun doneNavigating() {
        _navigateToVoterInfoFragment.value = null
    }

}