package com.example.locationsharejob3f

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.locationsharejob3f.ViewModel.AuthenticationViewModel
import com.example.locationsharejob3f.ViewModel.FireStoreViewModel
import com.example.locationsharejob3f.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var authViewModel : AuthenticationViewModel
    private lateinit var fireStoreViewModel: FireStoreViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        fireStoreViewModel = ViewModelProvider(this).get(FireStoreViewModel::class.java)
        binding.registerBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val name = binding.nameEt.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            authViewModel.register(email,password,{

                val location = ""

                fireStoreViewModel.saveUser(authViewModel.getCurrentUserId(),name,email,location)
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                finish()
            }, { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            })


        }

    }
}