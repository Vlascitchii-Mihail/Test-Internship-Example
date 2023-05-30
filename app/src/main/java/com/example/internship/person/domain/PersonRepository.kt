package com.example.internship.person.domain

import androidx.lifecycle.LiveData
import com.example.internship.person.data.Person
import kotlinx.coroutines.flow.StateFlow

interface PersonRepository {
    val networkError: LiveData<String>
    val showProgress: LiveData<Unit>
    val hideProgress: LiveData<Unit>
    val addPerson: LiveData<Unit>
    val allPerson: StateFlow<List<Person>>

    suspend fun getFullStaff()

    suspend fun addPerson(person: Person)
}