package com.example.bopit

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.solo_layout.*


class Solo : AppCompatActivity(), SensorEventListener{

    private var randomNum: Int = 0
    private var keepplaying: Boolean = true
    var startTime: Int = 0
    var currentTime: Int = 0
    val interval: Int = 1
    var lastActionTime: Long = 0

    // sensor related
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.solo_layout)

        btnStart.setOnClickListener {
            start_game()
        }

        // get sensor manager
        this.sensorManager =getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // get accelerometer sensor and register it
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            this.accelerometer = it
        }
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

    }

    fun start_game() {

    }

    private fun displayInstruction(rd: Int) {
        when (rd) {
            1 -> { textViewAction.text = "Shake it!" }

            2 -> { textViewAction.text = "Spin it" }

            3 -> { textViewAction.text = "Flip it"}

            4 -> { textViewAction.text = "Tap it"}
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.timestamp - lastActionTime < interval) {

        }
    }


}