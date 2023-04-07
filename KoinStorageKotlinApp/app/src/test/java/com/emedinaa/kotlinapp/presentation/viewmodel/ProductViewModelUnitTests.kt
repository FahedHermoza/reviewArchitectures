package com.emedinaa.kotlinapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emedinaa.kotlinapp.domain.usecase.AddProductUseCase
import com.emedinaa.kotlinapp.domain.usecase.ClearProductUseCase
import com.emedinaa.kotlinapp.domain.usecase.FetchProductUseCase
import com.emedinaa.kotlinapp.domain.usecase.UpdateProductUseCase
import com.emedinaa.kotlinapp.domian.usecase.mock.RepositoryMock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import kotlin.time.ExperimentalTime

/***
 * https://stackoverflow.com/questions/48049131/cannot-resolve-symbol-instanttaskexecutorrule/56073388#56073388
 *
 */

@ExperimentalTime
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProductViewModelUnitTests {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()

    private val fetchProductUseCase = Mockito.mock(FetchProductUseCase::class.java)
    private val clearProductUseCase = Mockito.mock(ClearProductUseCase::class.java)
    private val addProductUseCase = Mockito.mock(AddProductUseCase::class.java)
    private val updateProductUseCase = Mockito.mock(UpdateProductUseCase::class.java)

    private lateinit var productViewModel: ProductViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    private fun buildViewModel() {
        productViewModel = ProductViewModel(
            fetchProductUseCase,
            clearProductUseCase,
            addProductUseCase,
            updateProductUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    //https://developer.android.com/kotlin/flow/test?hl=es-419
    // Will be feature in the future
    @Test
    fun `init loadProduct with list`()= runTest {}

    @Test
    fun `check addNewProduct is add product`()= runTest {
        //given
        val mock = RepositoryMock.product()
        mock.logo = 2131558400 //Icon by default
        //when
        buildViewModel()
        productViewModel.addNewProduct(title = mock.name, cost = mock.cost, description = mock.description)
        advanceUntilIdle()

        //then
        verify(addProductUseCase, times(1)).invoke(AddProductUseCase.Params(mock))
    }

    @Test
    fun `check editProduct is update product`()= runTest {
        //given
        val mock = RepositoryMock.product()

        //when
        buildViewModel()
        productViewModel.editProduct(title = mock.name, cost = mock.cost, product = mock)
        advanceUntilIdle()

        //then
        verify(updateProductUseCase, times(1)).invoke(UpdateProductUseCase.Params(mock))
    }

    @Test
    fun `check deleteAllProducts is correct`()= runTest {
        //given
        //when
        buildViewModel()
        productViewModel.deleteAllProducts()
        advanceUntilIdle()

        //then
        verify(clearProductUseCase, times(1)).invoke()
    }

}