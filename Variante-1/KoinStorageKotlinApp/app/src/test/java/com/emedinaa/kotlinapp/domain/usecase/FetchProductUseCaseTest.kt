package com.emedinaa.kotlinapp.domian.usecase

import com.emedinaa.kotlinapp.domain.ProductRepository
import com.emedinaa.kotlinapp.domain.usecase.FetchProductUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FetchProductUseCaseTest {
    // UseCase
    private lateinit var useCase: FetchProductUseCase
    // Mocks
    private val repository = Mockito.mock(ProductRepository::class.java)

    @Before
    fun setup() {
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildUseCase(repository: ProductRepository = this.repository) {
        useCase = FetchProductUseCase(repository)
    }

    @Test
    fun`check_invoke`() = runTest{
        //given

        //when
        buildUseCase()
        useCase.invoke()
        advanceUntilIdle()

        //then
        Mockito.verify(repository, Mockito.times(1)).getAllProducts()
    }
}