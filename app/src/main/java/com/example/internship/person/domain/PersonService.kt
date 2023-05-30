package com.example.internship.person.domain

import com.example.internship.person.data.Person
import kotlinx.coroutines.flow.Flow

interface PersonService {

    fun getFullStaff(): Flow<List<Person>>

    suspend fun addPerson(person: Person)
}