package com.afsar.kotlintv.UI

import android.app.Activity
import android.os.Bundle
import com.afsar.kotlintv.R

/**
 * Loads [MainFragment].
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
