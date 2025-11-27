package com.example.locationsharejob3f.ViewModel

import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import com.example.locationsharejob3f.model.User
import com.google.firebase.firestore.FirebaseFirestore

class FireStoreViewModel: ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val userCollection = firestore.collection("users")
    fun saveUser(
        userId: String,
        name: String,
        email: String,
        location: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "location" to location
        )

        userCollection.document(userId).set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun saveUser(userId: String, name: String, email: String, location: String){
        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "location" to location
        )
        userCollection.document(userId).set(user)
            .addOnSuccessListener {  }
            .addOnFailureListener { e-> }
    }
    fun getAllUser(callback:(List<User>)-> Unit){
        userCollection.get()
            .addOnSuccessListener {result->
                val userlist = mutableListOf<User>()
                for(document in result){
                    val userId = document.id
                    val name = document.getString("name")?:""
                    val email = document.getString("email")?:""
                    val location = document.getString("location")?:""
                    userlist.add(User(userId,name,email,location))
                }
                callback(userlist)
            }
            .addOnFailureListener {  }
    }

    fun updateUser(userId: String,name: String,email: String,location: String){
        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "location" to location
        )
        val userMap = user.toMap()
        userCollection.document(userId).update(userMap)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }
    fun updateUserLocation(userId: String,location: String){
        if (userId.isEmpty()){
            return
        }
        val user = hashMapOf(
            "location" to location
        )
        val userMap = user.toMap()
        userCollection.document(userId).update(userMap)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }
    fun getUser(userId: String, callback: (User?) -> Unit){
        userCollection.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                callback(user)

            }
            .addOnFailureListener {
                callback(null)

            }

    }
    fun getUserLocation(userId: String,callback: (String) -> Unit){
        userCollection.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val location = documentSnapshot.getString("location")?: ""
                callback(location)
            }
            .addOnFailureListener { it
                callback("")
            }
    }

}