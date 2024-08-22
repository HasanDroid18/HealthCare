package com.example.healthcare.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.healthcare.Intro.BaseActivity
import com.example.healthcare.R
import com.example.healthcare.databinding.FragmentSettingsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        // Retrieve GoogleSignInClient configuration
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        setupLogoutButton()

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            userRef = database.getReference("Users").child(userId)

            // Fetch user data from Firebase
            userRef.get().addOnSuccessListener { snapshot ->
                val name = snapshot.child("name").value as? String
                val email = snapshot.child("email").value as? String

                // Set the values to the TextViews using binding
                binding.userNamePlaceholder.text = name ?: "Name not available"
                binding.userEmailPlaceholder.text = email ?: "Email not available"
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load user data.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "User not logged in.", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun setupLogoutButton() {
        binding.logoutBtn.setOnClickListener {
            // Sign out the user from Firebase
            FirebaseAuth.getInstance().signOut()

            // If Google sign-in was used, sign out from Google as well
            googleSignInClient.signOut().addOnCompleteListener {
                // Start BaseActivity to reset the state
                val intent = Intent(requireContext(), BaseActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

}
