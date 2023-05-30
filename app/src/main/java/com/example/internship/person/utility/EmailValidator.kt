package com.example.internship.person.utility

import javax.inject.Inject


class EmailValidator @Inject constructor(): Validator<String> {
    override fun isValid(data: String): Boolean {
        val regex = Regex("[A-Za-z\\d._-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,}")
        return regex.matches(data)
    }
}