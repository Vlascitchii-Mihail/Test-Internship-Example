package com.example.internship.person.domain

import com.example.internship.person.data.Person
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PersonServiceImp @Inject constructor(): PersonService {
    private var bank = mutableListOf(
        Person("Dennis", "Avalyn", "grade1","dennis@mail.ru"),
        Person("Chan", "Evie", "grade4","chan@mail.ru"),
        Person("Anton", "Burns", "grade2","anton@mail.ru"),
        Person("Buchanan", "Emerson", "grade2","buchanan@mail.ru"),
        Person("Reign", "Ramirez", "grade2","regin@mail.ru"),
        Person("Rachel", "Rosales", "grade2","rechel@mail.ru"),
    )

    override fun getFullStaff() = flow {
        delay(2_000L)
        emit(bank)
    }

    override suspend fun addPerson(person: Person){
        delay(2_000L)
        bank.add(person)
    }
}