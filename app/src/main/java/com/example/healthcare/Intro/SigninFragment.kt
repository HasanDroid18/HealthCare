package com.example.healthcare.Intro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.healthcare.MainActivity
import com.example.healthcare.Models.User
import com.example.healthcare.R
import com.example.healthcare.databinding.FragmentSigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase

class SigninFragment : Fragment() {

    private lateinit var binding: FragmentSigninBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.signinBtn.setOnClickListener {
            val email = binding.signupEmail.text.toString().trim()
            val password = binding.signupPassword.text.toString().trim()
            val confirmPassword = binding.signupConfirmPass.text.toString().trim()
            val name = binding.signupName.text.toString().trim()  // Assuming you have an EditText for name

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && name.isNotEmpty()) {
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = firebaseAuth.currentUser?.uid
                            val userRef = database.getReference("Users").child(userId!!)

                            val user = User(name, email)

                            userRef.setValue(user).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val intent = Intent(requireContext(), MainActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                } else {
                                    Toast.makeText(requireContext(), "Failed to save user data.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            if (task.exception is FirebaseAuthUserCollisionException) {
                                Toast.makeText(requireContext(), "This email is already registered.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Fields cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirectText.setOnClickListener {
            it.findNavController().navigate(R.id.action_signinFragment_to_loginFragment)
        }

        return binding.root
    }
}
