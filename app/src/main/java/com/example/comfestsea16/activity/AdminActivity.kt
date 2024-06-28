package com.example.comfestsea16.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comfestsea16.authentication.login.LoginActivity
import com.example.comfestsea16.databinding.ActivityAdminBinding
// Updated import to ListServiceAdmin
import com.example.comfestsea16.fragment.home.Service
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private val list = ArrayList<Service>()
    private val db = FirebaseFirestore.getInstance()


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView and Adapter (use ListServiceAdmin)
        binding.serviceList.layoutManager = LinearLayoutManager(this)
        val listServiceAdapter = ListServiceAdmin(list) // Changed to ListServiceAdmin
        binding.serviceList.adapter = listServiceAdapter

        binding.backButton.setOnClickListener(){
            val intent =
                Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.addServiceButton.setOnClickListener {
            val name = binding.serviceNameInput.text.toString().trim()
            val description = binding.descriptionInput.text.toString().trim() // You might want to change the input type to "text" in your XML

            if (name.isNotEmpty() && description.isNotEmpty()) {
                val newService = hashMapOf(
                    "name" to name,
                    "description" to description
                    // Add "imageUrl" field when you have that feature
                )

                db.collection("services")
                    .add(newService)
                    .addOnSuccessListener { documentReference ->
                        // Add the new service to the list and update the adapter
                        list.add(Service(documentReference.id, name, description, null))
                        listServiceAdapter.notifyItemInserted(list.size - 1)

                        // Clear input fields
                        binding.serviceNameInput.text.clear()
                        binding.descriptionInput.text.clear()

                        Toast.makeText(this, "Service added successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                        Toast.makeText(this, "Error adding service: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }



        // Fetch data from Firestore (same as before)
        val db = FirebaseFirestore.getInstance()
        db.collection("services")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val service = Service(
                        document.id,
                        document.getString("name"),
                        document.getString("description"),
                        document.getString("imageUrl")
                    )
                    list.add(service)
                }
                listServiceAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle errors (e.g., show a toast message)
                // Log.e("FirestoreError", "Error getting documents: ", exception)
            }
    }
}
