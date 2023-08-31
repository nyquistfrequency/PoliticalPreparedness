package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel: ViewModel() {

    //Establish live data for representatives and address
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val line1 = MutableLiveData<String>()
    private val line2 = MutableLiveData<String>()
    private val city = MutableLiveData<String>()
    val state = MutableLiveData<String>() // not private since it's passed through for mapAddressThroughState
    private val zip = MutableLiveData<String>()

    // No repository because no Database is being utilized
    fun getRepresentatives(address: String){
        viewModelScope.launch {
            val (offices,officials) = CivicsApi.retrofitService.getRepresentatives(address)
            _representatives.value = offices.flatMap {
                    office -> office.getRepresentatives(officials)
            }
        }
    }


    fun getAddressFromLocation(location: Address?){
        _address.value = location!!
    }

    // Enables to push "Find my representatives" with a state only if the user has not provided a full address
    fun mapAddressThroughState(state: String) {
        _address.value = Address(
            line1.value ?: "",
            line2.value ?: "",
            city.value ?: "",
            state,
            zip.value ?: ""
        )
    }

}
