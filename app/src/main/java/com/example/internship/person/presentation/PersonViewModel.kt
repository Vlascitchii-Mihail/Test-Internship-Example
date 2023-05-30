package com.example.internship.person.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internship.person.data.Person
import com.example.internship.person.domain.PersonRepository
import com.example.internship.person.utility.ActionLiveData
import com.example.internship.person.utility.CustomLogger
import com.example.internship.person.utility.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MESSAGE_LOG_NO_INTERNET_CONNECTION = "No internet connection"
private const val MESSAGE_LOG_INVALID_DATA = "Invalid data"

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val personRepo: PersonRepository,
    private val emailValidator: Validator<String>,
    private val customLogger: CustomLogger.Companion,
) : ViewModel() {

    private var _showErrorInternetConnection = ActionLiveData<Unit>()
    val showErrorInternetConnection: LiveData<Unit> = _showErrorInternetConnection

    private var _showErrorInvalidData = ActionLiveData<Unit>()
    val showErrorInvalidData: LiveData<Unit> = _showErrorInvalidData

    val allPerson = personRepo.allPerson
    val addPerson = personRepo.addPerson
    val networkError = personRepo.networkError
    val showProgress = personRepo.showProgress
    val hideProgress = personRepo.hideProgress

    fun getFullStaff(isNetwork: Boolean) {
        if (isNetwork) {
            viewModelScope.launch {
                personRepo.getFullStaff()
            }
        } else {
            _showErrorInternetConnection.postValue(Unit)
            customLogger.e(MESSAGE_LOG_NO_INTERNET_CONNECTION)
        }
    }

    fun addPerson(isNetwork: Boolean, person: Person) {
        if (isNetwork) {
            if (emailValidator.isValid(person.email) && person.name.isNotBlank()) {
                viewModelScope.launch {
                    personRepo.addPerson(person)
                }
            } else {
                _showErrorInvalidData.postValue(Unit)
                customLogger.e(MESSAGE_LOG_INVALID_DATA)
            }
        } else {
            _showErrorInternetConnection.postValue(Unit)
            customLogger.e(MESSAGE_LOG_NO_INTERNET_CONNECTION)
        }
    }
}