package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter

class RepresentativeFragment : Fragment() {

    companion object {
        private val TAG = RepresentativeFragment::class.java.simpleName
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    private val representativeviewModel: RepresentativeViewModel by lazy {
        val activity = requireNotNull(this.activity)
        val viewModelFactory = RepresentativeViewModelFactory(activity.application)
        ViewModelProvider(this, viewModelFactory)[RepresentativeViewModel::class.java]
    }

    //ToDo: Rework adapter - it's not just a click on the representatives but a click on of the elements (website, facebook, twitter)
    private val representativeListAdapter =
        RepresentativeListAdapter(RepresentativeListAdapter.RepresentativeClickListener { representative ->
            representativeviewModel.onRepresentativeClicked(representative)
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRepresentativeBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.representativeViewModel = representativeviewModel

        //TODO: Define and assign Representative adapter
        binding.representativeRecycler.adapter = representativeListAdapter

        //TODO: Populate Representative adapter

        //TODO: Establish button listeners for field and location search
        //TODO: On Button Press:
        //            if (checkLocationPermission()) {
        //            getlocation
        //        } else {
        //            requestQPermission()
        //        }
        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                //TODO: Determine what should be executed here
                Log.i(TAG, "Test")
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.permission_denied_explanation,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun requestQPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            //ToDo: enable my location without utilizing the map
            getLocation()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //TODO: REVIEW IF THIS WORKS AS INTENDED
            requestQPermission()
            false
        }
    }


    private fun isPermissionGranted(): Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return (
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                )
    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    }

    //TODO: Rework because it currently won't compile
//    private fun geoCodeLocation(location: Location): Address {
//        val geocoder = Geocoder(context, Locale.getDefault())
//        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
//                .map { address ->
//                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
//                }
//                .first()
//    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}