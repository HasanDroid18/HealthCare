package com.example.healthcare.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.healthcare.DependencyInjection.DaggerApplicationComponent2
import com.example.healthcare.HospitalRetro.HospitalService
import com.example.healthcare.R
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
        fetchHospitalData() // Only call this after the map is ready
        // Optionally, adjust the initial zoom level
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(5f))  // Adjust the zoom level based on the density of hospitals
        Log.d("MapFragment", "Map is ready")
    }


    private fun fetchHospitalData() {
        binding.progressBar.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = hospitalService.getHospitals()

                if (response.isSuccessful) {
                    val hospitalsList = response.body()?.listIterator()
                    val boundsBuilder = LatLngBounds.Builder()

                    hospitalsList?.forEach { hospitalItem ->
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
                } else {
                    Log.e("MapFragment", "Failed to fetch hospitals: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MapFragment", "Error fetching hospitals", e)
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }


}
