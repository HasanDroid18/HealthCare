package com.example.healthcare.Fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.healthcare.Adapters.SliderAdapter
import com.example.healthcare.EditProfileActivity
import com.example.healthcare.Models.SliderItems
import com.example.healthcare.ScanMain
import com.example.healthcare.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    // Firebase Database reference
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    // Handler for auto-scrolling
    private val handler = Handler(Looper.getMainLooper())
    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            val currentItem = binding.viewpager2.currentItem
            val itemCount = binding.viewpager2.adapter?.itemCount ?: 0

            if (itemCount > 0) {
                binding.viewpager2.setCurrentItem((currentItem + 1) % itemCount, true)
            }
            handler.postDelayed(this, 3000) // Adjust delay as needed
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize view binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("Banners")

        // Initialize the banner
        initBanner()
        nextActivity()
        chatFragment()

        return binding.root
    }

    private fun nextActivity(){
        binding.NextActBtn.setOnClickListener {
            val intent = Intent(requireContext(), ScanMain::class.java)
            startActivity(intent)
        }
    }

    private fun chatFragment(){
        binding.chaticon.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        val items = ArrayList<SliderItems>()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (issue in snapshot.children) {
                        val item = issue.getValue(SliderItems::class.java)
                        item?.let { items.add(it) }
                    }

                    banners(items)
                    binding.progressBarBanner.visibility = View.GONE

                    // Start auto-scrolling after banners are loaded
                    handler.postDelayed(autoScrollRunnable, 3000) // Adjust delay as needed
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors here
            }
        })
    }

    private fun banners(items: ArrayList<SliderItems>) {
        binding.viewpager2.adapter = SliderAdapter(items, binding.viewpager2)
        binding.viewpager2.clipChildren = false
        binding.viewpager2.clipToPadding = false
        binding.viewpager2.offscreenPageLimit = 3
        binding.viewpager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }

        binding.viewpager2.setPageTransformer(compositePageTransformer)
    }

    override fun onPause() {
        super.onPause()
        // Stop auto-scrolling when the fragment is not visible
        handler.removeCallbacks(autoScrollRunnable)
    }

    override fun onResume() {
        super.onResume()
        // Resume auto-scrolling when the fragment becomes visible again
        handler.postDelayed(autoScrollRunnable, 3000)
    }
}
