package com.e.speedy.runTracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.e.speedy.R
import com.e.speedy.database.RunDatabase
import com.e.speedy.databinding.FragmentRunTrackerBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 */
class RunTrackerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentRunTrackerBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_run_tracker,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = RunDatabase.getInstance(application).runDatabaseDAO
        val viewModelFactory = RunTrackerViewModelFactory(dataSource, application)
        val runTrackerViewModel =
            ViewModelProvider(this, viewModelFactory).get(RunTrackerViewModel::class.java)

        binding.runTrackerViewModel = runTrackerViewModel
        binding.lifecycleOwner = this
        val adapter = RunAdapter()
        binding.runList.adapter = adapter

        runTrackerViewModel.allRuns.observe(viewLifecycleOwner, Observer { runs ->
            runs?.let {
                adapter.submitList(runs)
            }
        })

        runTrackerViewModel.navigateToRunQuality.observe(viewLifecycleOwner, Observer { run ->
            run?.let {
                this.findNavController().navigate(
                    RunTrackerFragmentDirections.actionRunTrackerFragmentToRunQualityFragment(run.runId)
                )
                runTrackerViewModel.doneNavigating()
            }
        })

        runTrackerViewModel.showSnackbarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT
                ).show()
                runTrackerViewModel.doneShowingSnackbar()
            }
        })
        return binding.root
    }

}
