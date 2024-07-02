package com.example.comfestsea16.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.comfestsea16.authentication.login.LoginActivity
import com.example.comfestsea16.databinding.FragmentThirdBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ThirdFragment : Fragment() {

    private lateinit var binding : FragmentThirdBinding
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThirdBinding.inflate(inflater, container, false)
        firebaseFirestore = FirebaseFirestore.getInstance()

        val userId = FirebaseAuth.getInstance().currentUser?.uid


        userId?.let { uid ->
            firebaseFirestore.collection("user")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name")
                        val number = document.getString("number")
                        val email = FirebaseAuth.getInstance().currentUser?.email

                        binding.fullName.setText(name)
                        binding.phoneNumber.setText(number)
                        binding.email.setText(email)
                    }
                }
                .addOnFailureListener {

                }
        }

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}
