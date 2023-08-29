package com.example.android.politicalpreparedness.election

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : Fragment() {

    private val args: VoterInfoFragmentArgs by navArgs()

    private val voterInfoViewModel: VoterInfoViewModel by lazy {
        val activity = requireNotNull(this.activity)
        val viewModelFactory = VoterInfoViewModelFactory(activity.application, args.argElectionId)
        ViewModelProvider(this, viewModelFactory)[VoterInfoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View? {

        val binding = FragmentVoterInfoBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.voterInfoViewModel = voterInfoViewModel

        // Just for testing
        voterInfoViewModel.crosscheckIfIdPassedThrough()

        binding.followUnfollowButton.setOnClickListener {
            // Toggle the isSaved value of the election
            voterInfoViewModel.toggleElectionSavedStatus()
        }

        voterInfoViewModel.followButtonText.observe(viewLifecycleOwner) { buttonText ->
            binding.followUnfollowButton.text = buttonText
        }


//        binding.stateLocations.setOnClickListener {
//            val urlStr = voterInfoViewModel.voterInfo.value?.votingLocationUrl
//            urlStr?.run {
//                startActivityUrlIntent(this)
//            }
//        }
//
//        binding.stateBallot.setOnClickListener {
//            val urlStr = viewModel.voterInfo.value?.ballotInformationUrl
//            urlStr?.run {
//                startActivityUrlIntent(this)
//            }
//        }

        // TODO: Add ViewModel values and create ViewModel

        // TODO: Add binding values

        // TODO: Populate voter info -- hide views without provided data.

        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */

        // TODO: Handle loading of URLs

        // TODO: Handle save button UI state
        // TODO: cont'd Handle save button clicks
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        voterInfoViewModel..observe(viewLifecycleOwner) { elections ->
//            upcomingElectionsAdapter.submitList(elections)
//        }
    }

    // TODO: Create method to load URL intents
    private fun startActivityUrlIntent(urlStr: String) {
        val uri: Uri = Uri.parse(urlStr)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            intent.setPackage("com.android.chrome")
            startActivity(intent)

        } catch (e: ActivityNotFoundException) {

            try {
                intent.setPackage(null)
                startActivity(intent)

            } catch (e: ActivityNotFoundException) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.no_web_browser_found),
                    Snackbar.LENGTH_LONG).show()
            }
        }
    }
}