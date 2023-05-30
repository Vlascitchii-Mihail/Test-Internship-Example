package com.example.internship.person.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(
    val name: String,
    val surName: String,
    val grade: String,
    val email: String
) : Parcelable
