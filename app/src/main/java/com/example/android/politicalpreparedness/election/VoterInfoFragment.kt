package com.example.android.politicalpreparedness.election

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private val args: VoterInfoFragmentArgs by navArgs()

    private val voterInfoViewModel: VoterInfoViewModel by lazy {
        val activity = requireNotNull(this.activity)
        val viewModelFactory = VoterInfoViewModelFactory(activity.application, args.election)
        ViewModelProvider(this, viewModelFactory)[VoterInfoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View? {

        val binding = FragmentVoterInfoBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.voterInfoViewModel = voterInfoViewModel

        // Observe the "isElectionSaved" state and manipulate the button text based on that
        voterInfoViewModel.isElectionSaved.observe(viewLifecycleOwner) { isSaved ->
            binding.followUnfollowButton.text = if (isSaved) {
                getString(R.string.unfollow_election)
            } else {
                getString(R.string.follow_election)
            }
        }

        // On Button press: Toggle the isSaved value of the election
        binding.followUnfollowButton.setOnClickListener {
            voterInfoViewModel.toggleElectionSavedStatus()
        }

        // On Button press: Start LoadURLIntentActivity for votingLocation
        binding.stateLocations.setOnClickListener {
            val urlString = voterInfoViewModel.voterInfo.value?.votingLocation
            urlString?.run {
                activityLoadUrlIntent(this)
            }
        }

        // On Button press: Start LoadURLIntentActivity for ballotinformation
        binding.stateBallot.setOnClickListener {
            val urlString = voterInfoViewModel.voterInfo.value?.ballotInformation
            urlString?.run {
                activityLoadUrlIntent(this)
            }
        }

        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */


        return binding.root
    }

    @SuppressLint("LogNotTimber")
    private fun activityLoadUrlIntent(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        try {
            startActivity(intent)
        } catch (err: java.lang.Exception){
            Log.e("voterInfoResponse", err.message.toString())
        }
    }
}