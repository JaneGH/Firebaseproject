package com.example.firebaseproject

import com.google.firebase.database.FirebaseDatabase

class RealtimeDb {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("users")

    fun addUser() {
        val user = mapOf(
            "name" to "Anna",
            "age" to 22
        )

        myRef.child("user1").setValue(user)
    }
}