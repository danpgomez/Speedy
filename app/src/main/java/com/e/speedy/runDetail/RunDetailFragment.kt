package com.e.speedy.runDetail

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
import com.e.speedy.databinding.RunDetailFragmentBinding

class RunDetailFragment : Fragment() {

    private lateinit var viewModel: RunDetailViewModel
    private lateinit var viewModelFactory: RunDetailViewModelFactory
    private lateinit var binding: RunDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.run_detail_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val application = requireNotNull(this.activity).application
        val dataSource = RunDatabase.getInstance(application).runDatabaseDAO
        val arguments = RunDetailFragmentArgs.fromBundle(requireArguments())
        viewModelFactory = RunDetailViewModelFactory(arguments.runKey, dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RunDetailViewModel::class.java)
        binding.runDetailViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToRunTracker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(RunDetailFragmentDirections.actionRunDetailFragmentToRunTrackerFragment())
                viewModel.doneNavigating()
            }
        })
    }

}
