package com.example.internship.person.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.internship.person.TestDispatchers
import com.example.internship.person.data.Person
import com.example.internship.person.domain.PersonRepository
import com.example.internship.person.utility.CustomLogger
import com.example.internship.person.utility.Validator
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PersonViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var personRepository: PersonRepository

    @RelaxedMockK
    lateinit var customLogger: CustomLogger.Companion

    @RelaxedMockK
    lateinit var emailValidator: Validator<String>

    @InjectMockKs
    private lateinit var viewModel: PersonViewModel

    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic(CustomLogger::class)
        testDispatchers = TestDispatchers()
        Dispatchers.setMain(testDispatchers.testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setup list with persons when network is available on view created`() = runTest {
        viewModel.getFullStaff(isNetwork = true)
        testDispatchers.testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { personRepository.getFullStaff() }
    }

    @Test
    fun `show network error  when network isn't available on view created`() {
        viewModel.getFullStaff(isNetwork = false)

        assertEquals(Unit, viewModel.showErrorInternetConnection.value)
        verify(exactly = 1) { customLogger.e("No internet connection") }
    }

    @Test
    fun `don't add new person when email is valid and name is not blank on save button click`() {
        val isNetwork = true
        val newPerson = Person(
            name = "Emerson",
            surName = "Buchanan",
            grade = "senior",
            email = "buchanan@mail.ru"
        )
        every { emailValidator.isValid(newPerson.email) } returns true

        runTest {
            viewModel.addPerson(person = newPerson, isNetwork = isNetwork)
            testDispatchers.testDispatcher.scheduler.advanceUntilIdle()

            coVerify(exactly = 1) { personRepository.addPerson(newPerson) }
        }
    }

    @Test
    fun `don't add new person when email is valid and name is blank on save button click`() {
        val isNetwork = true
        val newPerson = Person(
            name = "  ",
            surName = "Buchanan",
            grade = "senior",
            email = "buchanan@mail.ru"
        )
        every { emailValidator.isValid(newPerson.email) } returns true

        viewModel.addPerson(person = newPerson, isNetwork = isNetwork)

        assertEquals(Unit, viewModel.showErrorInvalidData.value)
    }

    @Test
    fun `don't add new person when email isn't valid and name is blank on save button click`() {
        val isNetwork = true
        val newPerson = Person(
            name = "  ",
            surName = "Buchanan",
            grade = "senior",
            email = "buchanan@mail.ru"
        )
        every { emailValidator.isValid(newPerson.email) } returns false

        viewModel.addPerson(person = newPerson, isNetwork = isNetwork)

        assertEquals(Unit, viewModel.showErrorInvalidData.value)
    }

    @Test
    fun `don't add new person when network isn't available on save button click`() {
        val isNetwork = false
        val newPerson = Person(
            name = "  ",
            surName = "Buchanan",
            grade = "senior",
            email = "buchanan@mail.ru"
        )
        every { emailValidator.isValid(newPerson.email) } returns false

        viewModel.addPerson(person = newPerson, isNetwork = isNetwork)

        assertEquals(Unit, viewModel.showErrorInternetConnection.value)
    }

    @Test
    fun `add new person when email is valid and name is not blank on save button click`() {
        val isNetwork = true
        val newPerson = Person(
            name = " Dennis ",
            surName = "Avalyn",
            grade = "junior",
            email = "dennis@mail.ru"
        )
        every { emailValidator.isValid(newPerson.email) } returns true

        runTest {
            viewModel.addPerson(person = newPerson, isNetwork = isNetwork)
            testDispatchers.testDispatcher.scheduler.advanceUntilIdle()

            coVerify(exactly = 1) { personRepository.addPerson(newPerson) }
        }
    }
}