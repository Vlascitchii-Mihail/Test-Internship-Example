package com.example.internship.person.domain

import androidx.lifecycle.LiveData
import com.example.internship.person.data.Person
import com.example.internship.person.utility.ActionLiveData
import com.example.internship.person.utility.CustomLogger
import com.example.internship.person.utility.DispatcherProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val MESSAGE_LOG_NEW_PERSON = "Added new person"

class PersonRepositoryImp @Inject constructor(
    private val personService: PersonService,
    private val customLogger: CustomLogger.Companion,
    private val dispatchers: DispatcherProvider
) : PersonRepository {
    private val _networkError = ActionLiveData<String>()
    override val networkError: LiveData<String> = _networkError

    private var _showProgress = ActionLiveData<Unit>()
    override val showProgress: LiveData<Unit> = _showProgress

    private var _hideProgress = ActionLiveData<Unit>()
    override val hideProgress: LiveData<Unit> = _hideProgress

    private var _addPerson = ActionLiveData<Unit>()
    override val addPerson: LiveData<Unit> = _addPerson

    private val _allPerson = MutableStateFlow(emptyList<Person>())
    override val allPerson: StateFlow<List<Person>> = _allPerson.asStateFlow()

    override suspend fun getFullStaff() {
        personService.getFullStaff()
            .onStart { _showProgress.postValue(Unit) }
            .onCompletion { _hideProgress.postValue(Unit) }
            .flowOn(dispatchers.io)
            .catch { _networkError.postValue(it.message) }
            .collectLatest { _allPerson.emit(it) }
    }

    override suspend fun addPerson(person: Person) {
        try {
            _showProgress.postValue(Unit)
            withContext(dispatchers.io) {
                personService.addPerson(person)
            }
            _addPerson.postValue(Unit)
            customLogger.d("$MESSAGE_LOG_NEW_PERSON $person")
        } catch (e: Exception) {
            _networkError.postValue(e.message)
        } finally {
            _hideProgress.postValue(Unit)
        }
    }
}