package com.example.mikelesimonieventfindercs414hw4

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.sqrt

interface ShakeListener {
    fun onShakeDetected()
}

class ShakeSensor(context: Context, private val listener: ShakeListener) : SensorEventListener {
    private val sensorManager: SensorManager
    private val accelerationThreshold: Double

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerationThreshold = MINIMUM_SENSITIVITY
    }

    fun registerShakeSensor() {
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometerSensor != null) {
            sensorManager.registerListener(
                this,
                accelerometerSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun unregisterShakeSensor() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val overallAcceleration = sqrt((x * x + y * y + z * z).toDouble())
            if (overallAcceleration > accelerationThreshold) {
                Log.d(TAG, "Shake detected!")
                listener.onShakeDetected()
            }
        }
    }

    companion object {
        private const val TAG = "com.example.mikelesimonieventfindercs414hw4.ShakeSensor"
        private const val MINIMUM_SENSITIVITY = 14.9
    }
}
