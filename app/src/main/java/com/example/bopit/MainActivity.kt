/*
The Main activity

Displays two buttons, one for single player and one for multiplayer
 */


package com.example.bopit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val num = SimpleDateFormat("ss.SSS", Locale.getDefault()).format(Date()).toDouble()
    Log.d("onCreate", (num * 1000).toString())
    //listener for solo
    btnSolo.setOnClickListener {
        //link to Solo activity
      val intent = Intent(this, Solo::class.java)
      startActivity(intent)
    }


    btnMulti.setOnClickListener {
      //link to multiplayer activity
      val multi_intent = Intent(this, Multi::class.java)
      startActivity(multi_intent)
    }

  }
}
