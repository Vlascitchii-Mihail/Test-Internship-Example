package com.example.internship.di

import com.example.internship.person.domain.PersonRepository
import com.example.internship.person.domain.PersonRepositoryImp
import com.example.internship.person.domain.PersonService
import com.example.internship.person.domain.PersonServiceImp
import com.example.internship.person.utility.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class PersonBinds {

    @Binds
    @Singleton
    abstract fun bindsPersonRepository(impl: PersonRepositoryImp): PersonRepository

    @Binds
    @Singleton
    abstract fun bindsPersonService(impl: PersonServiceImp): PersonService

    @Binds
    @Singleton
    abstract fun bindsEmailValidator(impl: EmailValidator): Validator<String>

    @Binds
    @Singleton
    abstract fun bindsMyDefaultDispatchers(impl: DefaultDispatchers): DispatcherProvider
}