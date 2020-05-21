package com.e.speedy.runQuality

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.e.speedy.R
import com.e.speedy.database.RunDatabase
import com.e.speedy.databinding.FragmentRunQualityBinding

/**
 * A simple [Fragment] subclass.
 */
class RunQualityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentRunQualityBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_run_quality,
            container,
            false
        )
        val application = requireNotNull(this.activity).application
        val arguments = RunQualityFragmentArgs.fromBundle(requireArguments())
        val dataSource = RunDatabase.getInstance(application).runDatabaseDAO
        val runQualityViewModelFactory = RunQualityViewModelFactory(arguments.runKey, dataSource)
        val runQualityViewModel = ViewModelProvider(this, runQualityViewModelFactory)
            .get(RunQualityViewModel::class.java)
        binding.runQualityViewModel = runQualityViewModel

        runQualityViewModel.navigateToRunTracker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(RunQualityFragmentDirections.actionRunQualityFragmentToRunTrackerFragment())
                runQualityViewModel.doneNavigating()
            }
        })
        return binding.root
    }

}
