package com.example.internship.person.utility

interface Validator<T> {
    fun isValid(data:T): Boolean
}