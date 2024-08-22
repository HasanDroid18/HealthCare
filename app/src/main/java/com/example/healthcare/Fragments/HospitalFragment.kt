package com.example.healthcare.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcare.Adapters.HospitalAdapter
import com.example.healthcare.DependencyInjection.DaggerApplicationComponent1
import com.example.healthcare.HospitalRetro.HospitalItem
import com.example.healthcare.HospitalRetro.HospitalService
import com.example.healthcare.HospitalRetro.Hospitals 
import com.example.healthcare.databinding.FragmentHospitalBinding
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class HospitalFragment : Fragment() {

    // View binding to access views in the layout file
    private lateinit var binding: FragmentHospitalBinding

    // Adapter for the RecyclerView to display hospital data
    private lateinit var hospitalAdapter: HospitalAdapter

    // ProgressBar to show loading state
    private lateinit var progressBar: ProgressBar

    // HospitalService will be injected by Dagger for making API calls
    @Inject
    lateinit var hospitalService: HospitalService

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

        // Fetch hospital data from the API
        fetchHospitalData()

        // Return the root view
        return binding.root
    }

    // Function to fetch hospital data from the API
    private fun fetchHospitalData() {
        // Show the progress bar while loading data
        progressBar.visibility = View.VISIBLE

        // Launch a coroutine to perform the network request on a background thread
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Make the API call to get the list of hospitals
                val response: Response<Hospitals> = hospitalService.getHospitals()

                if (response.isSuccessful) {
                    // If the response is successful, process the data
                    val hospitalsList = response.body()?.listIterator()

                    if (hospitalsList != null) {
                        val hospitalsDataList = mutableListOf<HospitalItem>()

                        // Iterate through the hospital items and add them to the list
                        while (hospitalsList.hasNext()) {
                            val hospitalsItem = hospitalsList.next()

                            val name = hospitalsItem.name ?: ""
                            val type = hospitalsItem.type ?: ""
                            val status = hospitalsItem.status ?: ""

                            hospitalsDataList.add(HospitalItem(name, type, status))
                        }

                        // Update the RecyclerView with the new data
                        hospitalAdapter.updateData(hospitalsDataList)
                    }
                } else {
                    // Log an error message if the response is not successful
                    Log.e("API Error", "Error: ${response.code()}")
                }
                // Hide the progress bar after loading is complete
                progressBar.visibility = View.GONE
            } catch (e: Exception) {
                // Log any exceptions that occur during the API call
                Log.e("API Error", "Exception: ${e.message}")
                progressBar.visibility = View.GONE
            }
        }
    }
}
