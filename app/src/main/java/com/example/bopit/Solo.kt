package com.example.bopit

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.solo_layout.*
import kotlin.random.Random
import java.text.SimpleDateFormat
import java.util.*


class Solo : AppCompatActivity() {

    private var randomNum: Int = 0
    private var keepplaying: Boolean = true
    var startTime: Int = 0
    var currentTime: Int = 0
    val interval: Int = 4


    val timer = object: CountDownTimer(4000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            keepplaying = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.solo_layout)

        go.setOnClickListener {
            start_game()
        }





    }

    fun start_game() {
        var i: Int = 0
        //loop
        do {
            startTime = SimpleDateFormat("ss", Locale.getDefault()).format(Date()).toInt()
            randomNum = Random.nextInt(1, 4)

            //function for displaying the instruction (takes the random number as input)

            displayInstruction(randomNum)

            do {
                currentTime = SimpleDateFormat("ss", Locale.getDefault()).format(Date()).toInt()
            } while (currentTime - startTime < interval)

            Log.d("PlayGame", "timer ran out")
            Log.d("Random", randomNum.toString())

            i++
        } while (i < 5)

    }

    private fun displayInstruction(rd: Int) {
        when (rd) {
            1 -> { shakeTxt.text = "Shake it!" }

            2 -> { shakeTxt.text = "Spin it" }

            3 -> { shakeTxt.text = "Flip it"}

            4 -> { shakeTxt.text = "Tap it"}
        }
    }



}