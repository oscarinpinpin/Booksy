package com.booksy.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.booksy.data.local.AppDatabase
import com.booksy.data.local.UserDao
import com.booksy.data.local.UserEntity
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockContext: Context
    private lateinit var mockDatabase: AppDatabase
    private lateinit var mockUserDao: UserDao
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockContext = mockk(relaxed = true)
        mockDatabase = mockk(relaxed = true)
        mockUserDao = mockk(relaxed = true)

        every { mockContext.applicationContext } returns mockContext
        every { AppDatabase.getDatabase(any()) } returns mockDatabase
        every { mockDatabase.userDao() } returns mockUserDao
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `cargar usuario debe mostrar datos del usuario`() = runTest {
        // mock usuario
        val user = UserEntity(
            id = 1L,
            name = "Test User",
            email = "test@test.com",
            password = "password123"
        )

        every { mockUserDao.getUser() } returns flowOf(user)

        viewModel = ProfileViewModel(mockContext)
        advanceUntilIdle()

        viewModel.name.test {
            assertEquals("Test User", awaitItem())
        }

        viewModel.email.test {
            assertEquals("test@test.com", awaitItem())
        }
    }

    @Test
    fun `logout debe eliminar usuario de la base de datos`() = runTest {
        val user = UserEntity(
            id = 1L,
            name = "Test User",
            email = "test@test.com",
            password = "password123"
        )

        every { mockUserDao.getUser() } returns flowOf(user)
        coEvery { mockUserDao.deleteUser() } just Runs

        viewModel = ProfileViewModel(mockContext)
        advanceUntilIdle()

        viewModel.logout()

        coVerify { mockUserDao.deleteUser() }
    }
}