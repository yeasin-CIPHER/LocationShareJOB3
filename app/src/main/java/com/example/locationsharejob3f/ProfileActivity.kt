package com.example.locationsharejob3f

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.locationsharejob3f.ViewModel.AuthenticationViewModel
import com.example.locationsharejob3f.ViewModel.FireStoreViewModel
import com.example.locationsharejob3f.ViewModel.LocationViewModel
import com.example.locationsharejob3f.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthenticationViewModel
    private lateinit var fireStoreViewModel: FireStoreViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        fireStoreViewModel = ViewModelProvider(this)[FireStoreViewModel::class.java]
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]

        binding.logoutImBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.homeImBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.button.setOnClickListener {
            val newName = binding.etName.text.toString().trim()
            val newLocation = binding.etLocation.text.toString().trim()

            if (newName.isEmpty() || newLocation.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            updateProfile(newName, newLocation)
        }

        loadUserInfo()
    }

    private fun loadUserInfo() {
        val currentUser = authViewModel.getCurrentUser() ?: run {
            Toast.makeText(this, "User Not Found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.etEmail.setText(currentUser.email)

        fireStoreViewModel.getUser(currentUser.uid) { user ->
            if (user == null) {
                Toast.makeText(this, "User Not Found", Toast.LENGTH_SHORT).show()
                finish()
                return@getUser
            }

            binding.etName.setText(user.name)

            fireStoreViewModel.getUserLocation(currentUser.uid) { location ->
                if (location.isNotEmpty()) binding.etLocation.setText(location)
            }
        }
    }


    private fun updateProfile( newName: String, newLocation: String) {
        val currentUser = authViewModel.getCurrentUser()

        if (currentUser != null) {
            val userId = currentUser.uid
            fireStoreViewModel.updateUser(userId, newName, newLocation)
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            // Handle the case where the current user is null
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
    }
}
