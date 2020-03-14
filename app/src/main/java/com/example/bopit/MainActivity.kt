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
        //link to Solo no
      val intent = Intent(this, Solo::class.java)
      startActivity(intent)
    }


    btnMulti.setOnClickListener {
      val multi_intent = Intent(this, Multi::class.java)
      startActivity(multi_intent)
    }

  }
}
