/*
The multiplayer activity

Prompts user for username, which is stored in the firebase database

If it is a new username, it adds it to the database

 */

package com.example.bopit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.multi_layout.*


class Multi : AppCompatActivity() {

    private var username: String = ""
    val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.multi_layout)

        btnEnter.setOnClickListener {
            username = userName.text.toString()
            val myRef = database.getReference("users/$username")

            myRef.setValue(username)

            //START INTENT
            val intent = Intent(this, GameCenter::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }
}

