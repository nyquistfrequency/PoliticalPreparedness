package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment : Fragment() {


    private val electionsViewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity)
        val viewModelFactory = ElectionsViewModelFactory(activity.application)
        ViewModelProvider(this, viewModelFactory)[ElectionsViewModel::class.java]
    }

    private val upcomingElectionsAdapter =
        ElectionListAdapter(ElectionListAdapter.ElectionClickListener { election ->
            electionsViewModel.onUpcomingElectionClicked(election)
        })

    private val savedElectionsAdapter =
        ElectionListAdapter(ElectionListAdapter.ElectionClickListener { election ->
            electionsViewModel.onSavedElectionClicked(election)
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View? {

        val binding = FragmentElectionBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.electionsViewModel = electionsViewModel
        binding.upcomingElectionsRecycler.adapter = upcomingElectionsAdapter
        binding.savedElectionsRecycler.adapter = savedElectionsAdapter


        //Currently argdivision is commented out in the navgraph - Won't be used?
        electionsViewModel.navigateToVoterInfoFragment.observe(
            viewLifecycleOwner,
            Observer { election ->
                election?.let {
                    this.findNavController().navigate(
                        ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                            it
                        )
                    )
                    electionsViewModel.doneNavigating()
                }
            })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        electionsViewModel.listOfUpcomingElections.observe(viewLifecycleOwner) { upcomingElections ->
            upcomingElectionsAdapter.submitList(upcomingElections)
        }

        electionsViewModel.listOfSavedElections.observe(viewLifecycleOwner) { savedElections ->
            savedElectionsAdapter.submitList(savedElections)
        }
    }

}