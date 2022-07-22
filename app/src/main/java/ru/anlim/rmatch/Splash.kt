package ru.anlim.rmatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}