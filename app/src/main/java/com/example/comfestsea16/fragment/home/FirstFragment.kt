package com.example.comfestsea16.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comfestsea16.CustomerSupportActivity
import com.example.comfestsea16.R
import com.example.comfestsea16.databinding.CardHomeLayoutBinding
import com.example.comfestsea16.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    private lateinit var rvService: RecyclerView
    private lateinit var binding: FragmentFirstBinding
    private lateinit var cardHomeBinding: CardHomeLayoutBinding
    private val list = ArrayList<Service>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        initializeData()
        setupRecyclerView()
        clickCustomerServiceButton()
    }

    private fun initializeViews() {
        rvService = binding.serviceRv
        rvService.setHasFixedSize(true)
        cardHomeBinding = CardHomeLayoutBinding.bind(binding.cardView.root)
    }

    private fun initializeData() {
        list.addAll(getListService())
    }

    private fun setupRecyclerView() {
        rvService.layoutManager = LinearLayoutManager(requireContext())
        val listServiceAdapter = ListServiceAdapter(list)
        rvService.adapter = listServiceAdapter
        listServiceAdapter.setOnItemClickCallback(object : ListServiceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Service) {
                showSelectedService(data)
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
        return listService
    }

    private fun showSelectedService(service: Service) {
        Toast.makeText(requireContext(), "Kamu memilih ${service.name}", Toast.LENGTH_SHORT).show()
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
    }
}
