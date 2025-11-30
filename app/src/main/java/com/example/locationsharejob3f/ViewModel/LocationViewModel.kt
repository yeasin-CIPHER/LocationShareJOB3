package com.example.locationsharejob3f.ViewModel

import android.Manifest
import android.media.MediaPlayer
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnCompleteListener
import javax.security.auth.callback.Callback

class LocationViewModel: ViewModel() {
    private var fusedLocationClient: FusedLocationProviderClient?=null
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getLastLocation(callback:(String)-> Unit){
        fusedLocationClient?.lastLocation
            ?.addOnCompleteListener (OnCompleteListener { task ->
                if(task.isSuccessful && task.result!= null){
                    val lastLocation = task.result
                    val latitude = lastLocation.latitude
                    val longitude = lastLocation.longitude
                    val location = "Lat : $latitude , Long: $longitude"
                    callback(location)
                }
                else{
                    callback("Location not available")
                }
            })
    }
    fun initializeFusedLocation(client: FusedLocationProviderClient){
        fusedLocationClient = client
    }
}