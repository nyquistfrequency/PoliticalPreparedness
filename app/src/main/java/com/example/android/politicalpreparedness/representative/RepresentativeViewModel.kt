package com.example.android.politicalpreparedness.representative

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RepresentativeViewModel : ViewModel() {

    //Establish live data for representatives and address
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    val line1 = MutableLiveData<String>()
    val line2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<String>()

    // No repository because no Database is being utilized
    fun getRepresentatives(address: String) {
        viewModelScope.launch {
            try {
                val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(address)
                _representatives.value = offices.flatMap { office ->
                    office.getRepresentatives(officials)
                }
            }
            catch (err: HttpException){
                Log.e("getRepresentatives", "The Representatitves for this state were not found")
            }
        }
    }


        fun getAddressFromLocation(location: Address?) {
            _address.value = location!!

            // Added after Feedback from Mentor https://knowledge.udacity.com/questions/1006088
            line1.value = location.line1
            line2.value = location.line2!!
            city.value = location.city
            state.value = location.state
            zip.value = location.zip
    }

//    // No longer necessary because of the Mutable LiveData ImplementationUpdate currentState after getting location
//    fun getAddressState(address: Address?): String {
//        return address?.state ?: ""
//    }

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