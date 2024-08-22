package com.example.healthcare.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.healthcare.DependencyInjection.DaggerApplicationComponent2
import com.example.healthcare.HospitalRetro.HospitalItem
import com.example.healthcare.HospitalRetro.HospitalService
import com.example.healthcare.R
import com.example.healthcare.ViewModel.MapViewModel
import com.example.healthcare.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var googleMap: GoogleMap

    @Inject
    lateinit var hospitalService: HospitalService

    // ViewModel for managing map-related data
    private val mapViewModel: MapViewModel by viewModels {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return MapViewModel(hospitalService) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Dagger and inject dependencies into this fragment
        DaggerApplicationComponent2.create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        mapViewModel.fetchHospitalData()

        // Observe LiveData from the ViewModel
        mapViewModel.hospitals.observe(viewLifecycleOwner, Observer { hospitalsList ->
            displayHospitalsOnMap(hospitalsList)
        })

        mapViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        mapViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMsg ->
            errorMsg?.let { Log.e("MapFragment", it) }
        })

        // Optionally, adjust the initial zoom level
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(5f))
        Log.d("MapFragment", "Map is ready")
    }

    private fun displayHospitalsOnMap(hospitalsList: List<HospitalItem>) {
        val boundsBuilder = LatLngBounds.Builder()

        hospitalsList.forEach { hospitalItem ->
            val latitude = hospitalItem.info?.latitude
            val longitude = hospitalItem.info?.longitude

            if (latitude != null && longitude != null) {
                val position = LatLng(latitude, longitude)
                googleMap.addMarker(MarkerOptions().position(position))
                boundsBuilder.include(position)
            } else {
                Log.e("MapFragment", "Invalid coordinates: $latitude, $longitude")
            }
        }

        // Adjust the camera to show all markers
        val bounds = boundsBuilder.build()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }
}
