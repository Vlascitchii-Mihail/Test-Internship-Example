package com.example.internship.person.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.internship.person.TestDispatchers
import com.example.internship.person.data.Person
import com.example.internship.person.utility.CustomLogger
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PersonRepositoryImpTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var service: PersonService

    private var repository: PersonRepository? = null

    @RelaxedMockK
    lateinit var customLogger: CustomLogger.Companion

    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        testDispatchers = TestDispatchers()
        Dispatchers.setMain(testDispatchers.testDispatcher)
        repository = PersonRepositoryImp(service, customLogger, testDispatchers)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        repository = null
    }

    @Test
    fun `setup list with persons when network is available on view created`() {
        val immutableRepository = requireNotNull(repository)
        val listOfStaff = getPersons()
        every { service.getFullStaff() } returns flow { emit(listOfStaff); }

        runTest {
            immutableRepository.getFullStaff()
            testDispatchers.testDispatcher.scheduler.advanceUntilIdle()

            immutableRepository.allPerson
                .onStart { assertEquals(Unit, immutableRepository.showProgress.value) }
                .test {
                    assertEquals(listOfStaff, awaitItem())
                    assertEquals(Unit, immutableRepository.hideProgress.value)
                }
        }
    }

    @Test
    fun `show error when throw network error on get staff data`() {
        val immutableRepository = requireNotNull(repository)
        val error = Exception("network error")
        every { service.getFullStaff() } returns flow<Nothing> { throw error }

        runTest {
            immutableRepository.getFullStaff()
            testDispatchers.testDispatcher.scheduler.advanceUntilIdle()

            immutableRepository.allPerson.test {
                assertEquals(error.message, immutableRepository.networkError.value)
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `add new persons when no network errors on add new person`() {
        val immutableRepository = requireNotNull(repository)
        val person = mockk<Person>(relaxed = true)

        runTest {
            immutableRepository.addPerson(person)
            testDispatchers.testDispatcher.scheduler.advanceUntilIdle()

            verify(exactly = 1) { runBlocking { service.addPerson(person) } }
            assertEquals(Unit, immutableRepository.showProgress.value)
            assertEquals(Unit, immutableRepository.hideProgress.value)
        }
    }

    @Test
    fun `show error when throw network on add new person`() {
        val immutableRepository = requireNotNull(repository)
        val person = mockk<Person>(relaxed = true)
        val error = Exception("network error")
        every { runBlocking { service.addPerson(person) } } throws error

        runTest {
            immutableRepository.addPerson(person)
            testDispatchers.testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(error.message, immutableRepository.networkError.value)
            assertEquals(Unit, immutableRepository.hideProgress.value)
        }
    }

    private fun getPersons() = mutableListOf(
        Person("Dennis", "Avalyn", "grade1", "dennis@mail.ru"),
        Person("Chan", "Evie", "grade4", "chan@mail.ru"),
        Person("Anton", "Burns", "grade2", "anton@mail.ru"),
        Person("Buchanan", "Emerson", "grade2", "buchanan@mail.ru"),
        Person("Reign", "Ramirez", "grade2", "regin@mail.ru"),
        Person("Rachel", "Rosales", "grade2", "rechel@mail.ru"),
    )
}