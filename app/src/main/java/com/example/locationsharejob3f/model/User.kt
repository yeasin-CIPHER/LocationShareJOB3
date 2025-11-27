package com.example.locationsharejob3f.model

import com.google.firebase.firestore.PropertyName

data class User(

    val userId: String,
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("email")
    @set:PropertyName("email")
    var email: String = "",

    @get:PropertyName("location")
    @set:PropertyName("location")
    var location: String= ""
){
    constructor():this(userId = "", name = "", email = "")
}
