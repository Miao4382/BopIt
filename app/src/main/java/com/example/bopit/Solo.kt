package com.example.bopit

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.solo_layout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue
import kotlin.random.Random


class Solo : AppCompatActivity(), SensorEventListener{
    private val initialInterval: Int = 4000

    private var instrNum: Int = 0
    private var keepPlaying: Boolean = true
    private var startTime: Int = 0
    private var currentTime: Int = 0
    private var interval: Int = initialInterval
    private var score: Int = 0

    // sensor related
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null

    // action state recording
    private var flipA: Boolean = false
    private var flipB: Boolean = false
    private var tapped: Boolean = false
    private var shaked: Boolean = false
    private var spinned: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.solo_layout)

        btnPlay.setOnClickListener { startGame() }
        imgViewBopitSolo.setOnClickListener { tapped = true }

        // get sensor manager
        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // get accelerometer sensor and register listener
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            this.accelerometer = it
        }
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        // get gyroscope and register listener
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let {
            this.gyroscope = it
        }
        gyroscope?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    /* start_game()
    *
    * */
    private fun startGame() {
        score = 0
        DoAsync{
            //loop
            do {
                // reset game state
                resetGameState()

                startTime = (SimpleDateFormat("ss.SSS", Locale.getDefault()).format(Date()).toDouble() * 1000).toInt()
                instrNum = Random.nextInt(1, 4)     // when instrNum == 4, action: spin it, not supported on physical phone

                //function for displaying the instruction (takes the random number as input)

                textViewAction.post{displayInstruction(instrNum)}

                do {
                    currentTime = (SimpleDateFormat("ss.SSS", Locale.getDefault()).format(Date()).toDouble() * 1000).toInt()
                    progressBar.progress = 100 - ((currentTime - startTime).toDouble() / interval * 100).toInt()
                } while (currentTime - startTime < interval)

                // check if action performed
                checkKeepPlay()

                // display feedback and update score
                if (keepPlaying) {
                    textViewScore.post {
                        displayRandomFeedBack()
                        score += 1
                        textViewScore.text = "Score: " + score.toString()
                    }
                }
            } while (keepPlaying)

            textViewAction.post {
                textViewAction.text = "You lose"
                textViewScore.text = "Final Score: " + score.toString()
                // reset interval
                interval = initialInterval
            }
        }
    }

    private fun resetGameState() {
        keepPlaying = false
        flipA = false
        flipB = false
        tapped = false
        shaked = false
        spinned = false

        // refill progress bar
        progressBar.progress = 100

        // increase difficulty based on current score
        if (interval > 1500)
            interval -= 300
    }

    private fun displayInstruction(rd: Int) {
        when (rd) {
            1 -> { textViewAction.setText("Shake it!") }

            2 -> { textViewAction.setText("Flip it!") }

            3 -> { textViewAction.setText("Tap it!") }

            4 -> { textViewAction.setText("Spin it!") }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        // event is returned from system, it has info about the sensor triggered the reading
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                if (event.values[0].absoluteValue > 2) {
                    shaked = true
                }

                if (event.values[2] < 0) {
                    flipA = true
                }

                if (event.values[2] > 0 && flipA) { // we already flipped once, this is flip back
                    flipB = true
                }

            }

            Sensor.TYPE_GYROSCOPE -> {
                if (event.values[2].absoluteValue > 2) {
                    spinned = true
                }
            }
        }
    }

    // displayRandomFeedBack(): will display random feed back on succeed, also update the score
    private fun displayRandomFeedBack() {
        // show toast
        when (Random.nextInt(1, 5)) {
            1 -> { Toast.makeText(this, "Good Job!", Toast.LENGTH_SHORT).show()}
            2 -> { Toast.makeText(this, "You're Awesome!", Toast.LENGTH_SHORT).show()}
            3 -> { Toast.makeText(this, "Fantastic!", Toast.LENGTH_SHORT).show()}
            4 -> { Toast.makeText(this, "Beautiful!", Toast.LENGTH_SHORT).show()}
            5 -> { Toast.makeText(this, "Well Done!", Toast.LENGTH_SHORT).show()}
        }

    }

    // checkKeepPlay(): will check if we continue play or not
    private fun checkKeepPlay() {
        when (instrNum) {
            1 -> {  // shake it
                keepPlaying = shaked
            }

            2 -> {  // flip it
                keepPlaying = flipA && flipB
            }

            3 -> {  // tap it (tap the toy)
                keepPlaying = tapped
            }

            4 -> {  // spin it (spin the phone along z-axis)
                keepPlaying = spinned
            }
        }
    }
}

class DoAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    init {
        execute()
    }

    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}
