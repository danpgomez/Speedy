package com.e.speedy.runTracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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
        val adapter = RunAdapter(RunListener {
            runId ->  runTrackerViewModel.onRunClicked(runId)
        })
        binding.runList.adapter = adapter

        val gridLayoutManager = GridLayoutManager(activity, 3)
        gridLayoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                    0 -> 3
                    else -> 1
                }
        }

        binding.runList.layoutManager = gridLayoutManager

        runTrackerViewModel.allRuns.observe(viewLifecycleOwner, Observer { runs ->
            runs?.let {
                adapter.addHeaderSubmitList(runs)
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

        runTrackerViewModel.navigateToRunDetail.observe(viewLifecycleOwner, Observer { run ->
            run?.let {
                this.findNavController().navigate(RunTrackerFragmentDirections.actionRunTrackerFragmentToRunDetailFragment(run))
                runTrackerViewModel.onRunDetailNavigated()
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
