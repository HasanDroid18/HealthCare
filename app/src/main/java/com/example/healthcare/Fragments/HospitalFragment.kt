package com.example.healthcare.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcare.Adapters.HospitalAdapter
import com.example.healthcare.DependencyInjection.DaggerApplicationComponent1
import com.example.healthcare.HospitalRetro.HospitalService
import com.example.healthcare.ViewModels.HospitalViewModel
import com.example.healthcare.databinding.FragmentHospitalBinding
import javax.inject.Inject

class HospitalFragment : Fragment() {

    // View binding to access views in the layout file
    private lateinit var binding: FragmentHospitalBinding

    // Adapter for the RecyclerView to display hospital data
    private lateinit var hospitalAdapter: HospitalAdapter

    // ProgressBar to show loading state
    private lateinit var progressBar: ProgressBar

    // Inject HospitalService
    @Inject
    lateinit var hospitalService: HospitalService

    // ViewModel for managing UI-related data
    private val hospitalViewModel: HospitalViewModel by viewModels {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return HospitalViewModel(hospitalService) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Dagger and inject dependencies into this fragment
        DaggerApplicationComponent1.create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentHospitalBinding.inflate(inflater, container, false)

        // Initialize the ProgressBar
        progressBar = binding.progressBarHospitals

        // Set up RecyclerView with a linear layout manager and the hospital adapter
        val recyclerView: RecyclerView = binding.hospitalsRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        hospitalAdapter = HospitalAdapter(emptyList()) // Initialize with an empty list
        recyclerView.adapter = hospitalAdapter

        // Observe LiveData from the ViewModel
        hospitalViewModel.hospitals.observe(viewLifecycleOwner, Observer { hospitalItems ->
            hospitalAdapter.updateData(hospitalItems)
        })

        hospitalViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        hospitalViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMsg ->
            errorMsg?.let { Log.e("API Error", it) }
        })

        // Fetch hospital data from the API
        hospitalViewModel.fetchHospitalData()

        // Return the root view
        return binding.root
    }
}
