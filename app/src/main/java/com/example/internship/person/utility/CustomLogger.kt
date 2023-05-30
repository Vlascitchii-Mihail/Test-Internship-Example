package com.example.internship.person.utility

import android.util.Log
import javax.inject.Inject

class CustomLogger @Inject constructor() {
    companion object {
        private const val TAG = "CustomLogger"

        fun d(message: String) {
            Log.d(TAG, "CustomLogger: Debug message $message")
            print("System Log: $message")
        }

        fun e(message: String) {
            Log.d(TAG, "CustomLogger: Error message $message")
            print("System Log: $message")
        }
    }
}