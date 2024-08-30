package com.example.healthcare

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.healthcare.databinding.ActivityMainBinding
import com.example.healthcare.Fragments.ChatFragment
import com.example.healthcare.Fragments.HomeFragment
import com.example.healthcare.Fragments.HospitalFragment
import com.example.healthcare.Fragments.MapFragment
import com.example.healthcare.Fragments.SettingsFragment



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.chat -> replaceFragment(ChatFragment())
                R.id.hospital -> replaceFragment(HospitalFragment())
                R.id.map -> replaceFragment(MapFragment())
                R.id.settings -> replaceFragment(SettingsFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}