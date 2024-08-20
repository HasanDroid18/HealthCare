package com.example.healthcare.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthcare.Intro.BaseActivity
import com.example.healthcare.R
import com.example.healthcare.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setupLogoutButton()

        return binding.root
    }

    private fun setupLogoutButton() {
        binding.logoutBtn.setOnClickListener {
            // Sign out the user
            FirebaseAuth.getInstance().signOut()

            // Start BaseActivity to reset the state
            val intent = Intent(requireContext(), BaseActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
