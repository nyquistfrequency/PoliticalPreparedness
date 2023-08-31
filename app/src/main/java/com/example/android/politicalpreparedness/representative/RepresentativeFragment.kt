package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.network.models.Address
import com.google.android.gms.location.*
import java.util.*
import androidx.lifecycle.Observer
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

class RepresentativeFragment : Fragment() {

    companion object {
        private val TAG = RepresentativeFragment::class.java.simpleName
        private const val REQUEST_LOCATION_PERMISSION = 1
        private var currentState: String = ""
    }

    // After Submission Feedback: Rewrote my binding so it's accessible outside of onCreateView so it can be utilized for onSavedInstance
    private lateinit var binding: FragmentRepresentativeBinding

    private val representativeViewModel: RepresentativeViewModel by lazy {
        val viewModelFactory = RepresentativeViewModelFactory()
        ViewModelProvider(this, viewModelFactory)[RepresentativeViewModel::class.java]
    }

    @SuppressLint("LogNotTimber")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepresentativeBinding.inflate(inflater)

        binding.executePendingBindings()
        binding.lifecycleOwner = this
        binding.representativeViewModel = representativeViewModel

        // Spinner created from the input in https://knowledge.udacity.com/questions/945408
        val representativeListAdapter = RepresentativeListAdapter()
        val spinnerAdapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,resources.getStringArray(R.array.states))
        binding.representativeRecycler.adapter = representativeListAdapter
        binding.state.adapter = spinnerAdapter

        // Populating the adapters through LiveData
        representativeViewModel.representatives.observe(viewLifecycleOwner, Observer {
            it?.let{
                representativeListAdapter.submitList(it)
            }
        })

        representativeViewModel.address.observe(viewLifecycleOwner, Observer {
            it?.let{
                representativeViewModel.getRepresentatives(it.toFormattedString())
            }
        })

        binding.buttonSearch.setOnClickListener {
            Log.i(TAG, "Find my representatives Button was pressed")
            hideKeyboard()
            binding.addressLine1.text.toString()
            binding.addressLine2.text.toString()
            binding.city.text.toString()
            binding.state.selectedItem.toString()
            binding.zip.toString()

            representativeViewModel.mapAddressThroughState(binding.state.selectedItem as String)
            binding.executePendingBindings()
        }


        binding.buttonLocation.setOnClickListener {
            Log.i(TAG, "Use my Location Button was pressed")
            if (checkLocationPermissions()) {
                Log.i(TAG, "checkLocationPermissions passed")
                getLocation()
                // set current State to Spinner
                binding.state.setSelection(spinnerAdapter.getPosition(currentState))
                binding.executePendingBindings()
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.permission_denied_explanation,
                    Toast.LENGTH_SHORT).show()
            }

        }


        //After Submission Feedback:
        //Added Parcelable address (as from https://knowledge.udacity.com/questions/616631)
        savedInstanceState?.getParcelable<Address>("address")?.let{
            representativeViewModel.getAddressFromLocation(it)
        }

        // Get MotionLayout State (as from https://knowledge.udacity.com/questions/873520)
        savedInstanceState?.getInt("motionLayout")?.let{
            binding.representativeMotionlayout.transitionToState(it)
        }

        return binding.root
    }

    //After Submission Feedback: Implementing onSaveInstance to persist MotionLayout & Address
    //(as from https://knowledge.udacity.com/questions/809749)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState called")
        //Persisting values for address
        outState.putParcelable("address",binding.representativeViewModel?.address?.value)

        // Persisting Motionlayout
        outState.putInt("motionLayout",binding.representativeMotionlayout.currentState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "getLocation is executed now")
                getLocation()

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
            Log.i(TAG, "requestQPermission passed")
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
            requestQPermission()
            false
        }
    }


    private fun isPermissionGranted(): Boolean {
        //Check if permission is already granted and return (true = granted, false = denied/other)
        return (
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                )
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        Log.i(TAG, "getLocation is called")
        LocationServices.getFusedLocationProviderClient(requireContext())
            .lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    try {
                        representativeViewModel.getAddressFromLocation(geoCodeLocation(it))
                        // Update currentState after getting location
                        currentState = representativeViewModel.getAddressState(geoCodeLocation(it))
                    } catch (err: IOException) {
                        Toast.makeText(
                            requireContext(),
                            R.string.error_getting_address,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


    private fun geoCodeLocation(location: Location): Address? {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            ?.map { address ->
                Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
            }
            ?.first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}