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

    private var instrNum: Int = 0
    private var keepPlaying: Boolean = true
    var startTime: Int = 0
    var currentTime: Int = 0
    val interval: Int = 4

    // sensor related
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    // action state recording
    private var flipA: Boolean = false
    private var flipB: Boolean = false


/*    val timer = object: CountDownTimer(4000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            keepplaying = false
        }
    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.solo_layout)

        go.setOnClickListener {
            start_game()
        }

        // get sensor manager
        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // get accelerometer sensor and register listener
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            this.accelerometer = it
        }
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

    }

    fun start_game() {
        doAsync{
            //loop
            do {
                // reset game state
                resetGameState()

                startTime = SimpleDateFormat("ss", Locale.getDefault()).format(Date()).toInt()
                instrNum = Random.nextInt(1, 3)

                //function for displaying the instruction (takes the random number as input)

                textViewAction.post({displayInstruction(instrNum)})

                do {
                    currentTime = SimpleDateFormat("ss", Locale.getDefault()).format(Date()).toInt()
                } while (currentTime - startTime < interval)

                Log.d("PlayGame", "timer ran out")
                Log.d("Random", instrNum.toString())

                when (instrNum) {
                    1 -> {  // shake it

                    }

                    2 -> {  // flip it
                        keepPlaying = flipA && flipB
                    }
                }

                if (keepPlaying)
                    textViewFeedBack.post { textViewFeedBack.text = textViewFeedBack.text.toString() + "!"}

            } while (keepPlaying)

//            shakeTxt.text = "You lose"
            textViewAction.post {textViewAction.text = "You lose"}
        }

    }

    private fun resetGameState() {
        keepPlaying = false
        flipA = false
        flipB = false
    }

    private fun displayInstruction(rd: Int) {
        when (rd) {
            1 -> {
                textViewAction.setText("Shake it!")
            }

            2 -> {
                textViewAction.setText("Flip it!")
            }

            3 -> {
                textViewAction.setText("Spin it!")
            }

            4 -> {
                textViewAction.setText("Tap it!")
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        // event is returned from system, it has info about the sensor triggered the reading
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                if (event.values[0].absoluteValue > 2) {
                    keepPlaying = true
                }

                if (event.values[2] < 0) {
                    flipA = true
                }

                if (event.values[2] > 0 && flipA) { // we already flipped once, this is flip back
                    flipB = true
                }

            }
        }
    }

}

class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    init {
        execute()
    }

    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}