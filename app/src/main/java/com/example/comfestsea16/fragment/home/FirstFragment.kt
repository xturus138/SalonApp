package com.example.comfestsea16.fragment.home

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comfestsea16.R
import com.example.comfestsea16.authentication.login.LoginActivity
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
    private lateinit var user: FirebaseUser
    private val list = ArrayList<Service>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
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
                    userText.text = finalUserName // Use name if available, else fallback to email
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        val logoutButton = binding.logoutSementara
        logoutButton.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            val intent=
                Intent(this@FirstFragment.requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

    }



    private fun initializeViews() {
        rvService = binding.serviceRv
        rvService.setHasFixedSize(true)
        cardHomeBinding = CardHomeLayoutBinding.bind(binding.cardView.root)
    }

    private fun initializeData() {
        list.clear()
        list.addAll(getListService())
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

    private fun getListService(): ArrayList<Service> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listService = ArrayList<Service>()
        for (i in dataName.indices) {
            val service = Service(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listService.add(service)
        }
        dataPhoto.recycle()
        return listService
    }

    private fun showSelectedService(service: Service) {
        Toast.makeText(requireContext(), "Kamu memilih ${service.name}", Toast.LENGTH_SHORT).show()
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
