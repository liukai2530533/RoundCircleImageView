package com.newki.sample

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.newki.circle_round.RoundCircleImageView

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_demo)

        RoundCircleImageView(this)
    }

}