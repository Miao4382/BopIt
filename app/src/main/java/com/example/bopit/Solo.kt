package com.example.bopit

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.solo_layout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue
import kotlin.random.Random


class Solo : AppCompatActivity(), SensorEventListener{

    private var randomNum: Int = 0
    private var keepPlaying: Boolean = true
    var startTime: Int = 0
    var currentTime: Int = 0
    val interval: Int = 2

    // sensor related
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null


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
                // reset keepplaying
                keepPlaying = false

                startTime = SimpleDateFormat("ss", Locale.getDefault()).format(Date()).toInt()
                randomNum = Random.nextInt(1, 5)

                //function for displaying the instruction (takes the random number as input)

                shakeTxt.post({displayInstruction(randomNum)})

                do {
                    currentTime = SimpleDateFormat("ss", Locale.getDefault()).format(Date()).toInt()
                } while (currentTime - startTime < interval)

                Log.d("PlayGame", "timer ran out")
                Log.d("Random", randomNum.toString())

            } while (keepPlaying)

//            shakeTxt.text = "You lose"
            shakeTxt.post {shakeTxt.text = "You lose"}
        }

    }

    private fun displayInstruction(rd: Int) {
        when (rd) {
            1 -> {
                shakeTxt.setText("Shake it!")
            }

            2 -> {
                shakeTxt.setText("Flip it!")
            }

            3 -> {
                shakeTxt.setText("Spin it!")
            }

            4 -> {
                shakeTxt.setText("Tap it!")
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