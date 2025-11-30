package com.example.locationsharejob3f

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locationsharejob3f.ViewModel.AuthenticationViewModel
import com.example.locationsharejob3f.ViewModel.FireStoreViewModel
import com.example.locationsharejob3f.ViewModel.LocationViewModel
import com.example.locationsharejob3f.databinding.ActivityMainBinding
import com.example.locationsharejob3f.model.UserAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var authViewModel : AuthenticationViewModel
    private lateinit var fireStoreViewModel: FireStoreViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var recyclerViewUser : RecyclerView
    private lateinit var  drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (result.all { it.value }) {
                getLocation()
            }
        }
    lateinit var binding: ActivityMainBinding
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        fireStoreViewModel = ViewModelProvider(this)[FireStoreViewModel::class.java]
        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            getLocation()
        }

        recyclerViewUser = binding.rrecyclerView
        userAdapter = UserAdapter(emptyList())
        recyclerViewUser.adapter = userAdapter

        recyclerViewUser.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        fetchUser()

        drawerLayout = binding.drawer
        navigationView = binding.navView

        actionBarDrawerToggle = ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {item ->
            when(item.itemId){
                R.id.profile ->{
                    startActivity(Intent(this, ProfileActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.logout->{
                    Firebase.auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
        binding.locationBtn.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }




    }
    private fun fetchUser(){
        fireStoreViewModel.getAllUser {userList->
            userAdapter.updateData(userList)

        }
    }

    private fun getLocation() {
        // Check permissions safely before requesting lastLocation
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return   // Permission not granted → stop safely
        }

        locationViewModel.getLastLocation { location ->
            authViewModel.getCurrentUserId()?.let { userId ->
                fireStoreViewModel.updateUserLocation(userId, location)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            true
        }
        else super.onOptionsItemSelected(item)
    }
}