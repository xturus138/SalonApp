package com.example.comfestsea16.fragment.home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comfestsea16.databinding.CardHomeLayoutBinding
import com.example.comfestsea16.databinding.FragmentFirstBinding
import com.example.comfestsea16.fragment.form.FormActivity
import com.example.comfestsea16.fragment.support.CustomerSupportActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class FirstFragment : Fragment() {
    private lateinit var rvService: RecyclerView
    private lateinit var binding: FragmentFirstBinding
    private lateinit var cardHomeBinding: CardHomeLayoutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var user: FirebaseUser
    private val list = ArrayList<Service>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        progressBar = binding.progressBar
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        initializeData()
        setupRecyclerView()
        clickCustomerServiceButton()

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        val userText = cardHomeBinding.userName
        val db = FirebaseFirestore.getInstance()
        db.collection("user").document(user.uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userName = document.getString("name")
                    val finalUserName = "Hi, ${userName ?: user.email}"
                    userText.text = finalUserName
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }



    private fun initializeViews() {
        rvService = binding.serviceRv
        rvService.setHasFixedSize(true)
        cardHomeBinding = CardHomeLayoutBinding.bind(binding.cardView.root)
    }

    private fun setupRecyclerView() {
        rvService.layoutManager = LinearLayoutManager(requireContext())
        val listServiceAdapter = ListServiceAdapter(list)
        rvService.adapter = listServiceAdapter
        listServiceAdapter.setOnItemClickCallback(object : ListServiceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Service) {
                showSelectedService(data)
                navigateItems()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeData() {
        list.clear()
        progressBar.visibility = View.VISIBLE

        val db = FirebaseFirestore.getInstance()
        db.collection("services").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val service = document.toObject(Service::class.java)
                    service.id = document.id
                    list.add(service)
                    rvService.adapter?.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                }
                rvService.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
                progressBar.visibility = View.GONE
            }
    }


    private fun showSelectedService(service: Service) {
        Toast.makeText(requireContext(), "Please select the service again in the form!", Toast.LENGTH_LONG).show()
    }

    private fun navigateItems(){
        val intent=
            Intent(this@FirstFragment.requireContext(), FormActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun clickCustomerServiceButton() {
        val customerServiceButton = cardHomeBinding.customerServiceButton
        customerServiceButton.setOnClickListener {
            navigateToCustomerSupport()
        }
    }

    private fun navigateToCustomerSupport() {
        val intent =
            Intent(this@FirstFragment.requireContext(), CustomerSupportActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
