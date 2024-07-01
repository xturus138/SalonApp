package com.example.comfestsea16.activity.admin

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

        binding.nextButton.setOnClickListener(){
            val intent =
                Intent(this, AccAdmin::class.java)
            startActivity(intent)
            finish()
        }


        binding.addServiceButton.setOnClickListener {
            val name = binding.serviceNameInput.text.toString().trim()
            val description = binding.descriptionInput.text.toString().trim()
            val durationString = binding.sessionDurationInput.text.toString().trim()
            val duration = if (durationString.isNotEmpty()) durationString.toIntOrNull() else null

            if (name.isNotEmpty() && description.isNotEmpty() && duration != null) {
                val newService = hashMapOf(
                    "name" to name,
                    "description" to description,
                    "sessionDuration" to duration
                )

                db.collection("services")
                    .add(newService)
                    .addOnSuccessListener { documentReference ->
                        list.add(Service(documentReference.id, name, description, null, duration))
                        binding.serviceList.adapter?.notifyItemInserted(list.size - 1)

                        binding.serviceNameInput.text.clear()
                        binding.descriptionInput.text.clear()
                        binding.sessionDurationInput.text.clear()

                        Toast.makeText(this, "Service added successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error adding service: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
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
                        document.getString("imageUrl"),
                        document.getLong("sessionDuration")?.toInt()
                    )
                    list.add(service)
                }
                binding.serviceList.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle errors (e.g., show a toast message)
            }
    }
}
