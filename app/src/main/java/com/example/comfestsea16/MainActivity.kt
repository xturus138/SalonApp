package com.example.comfestsea16

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var rvService: RecyclerView
    private val list = ArrayList<Service>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvService = findViewById(R.id.service_rv)
        rvService.setHasFixedSize(true)

        list.addAll(getListService())
        showRecyclerList()

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

    private fun showRecyclerList() {
        rvService.layoutManager = LinearLayoutManager(this)
        val listServiceAdapter = ListServiceAdapter(list)
        rvService.adapter = listServiceAdapter

        listServiceAdapter.setOnItemClickCallback(object : ListServiceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Service) {
                showSelectedService(data)
            }
        })
    }

    private fun showSelectedService(service: Service) {
        Toast.makeText(this, "Kamu memilih " + service.name, Toast.LENGTH_SHORT).show()
    }
}