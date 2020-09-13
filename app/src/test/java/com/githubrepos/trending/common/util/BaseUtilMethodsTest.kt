package com.githubrepos.trending.common.util

import com.githubrepos.trending.common.BaseTest
import com.githubrepos.trending.common.data.ApiResponse
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BaseUtilMethodsTest : BaseTest() {

    @MockK(relaxUnitFun = true)
    lateinit var randomClass: RandomClass

    @Test
    fun `Method apiResponseFrom should return ApiResponse Success on executing a method without exceptions`() {
        val list = listOf<Int>(1)
        coEvery { randomClass.performAction() } returns list
        runBlockingTest {
            var apiResponse = apiResponseFrom { randomClass.performAction() }
            assertTrue(apiResponse is ApiResponse.Success)
            assertEquals((apiResponse as ApiResponse.Success).data, list)
        }
    }

    @Test
    fun `Method apiResponseFrom should return ApiResponse Error on executing a method that throws exceptions`() {

        coEvery { randomClass.performAction() } throws RuntimeException("Test")

        runBlockingTest {
            var apiResponse = apiResponseFrom { randomClass.performAction() }
            assertTrue(apiResponse is ApiResponse.Error)
            assertEquals((apiResponse as ApiResponse.Error).message, "Test")
        }
    }

    @Test
    fun `Method apiResponseHandler  should execute success block when ApiResponse is Success`() {

        val apiResponse = listOf<Int>().asSuccess()

        apiResponseHandler(
            status = apiResponse,
            onSuccess = { randomClass.onSuccess() },
            onError = { randomClass.onError() }
        )

        assertTrue(apiResponse is ApiResponse.Success)
        verify(exactly = 1) { randomClass.onSuccess() }
        verify(exactly = 0) { randomClass.onError() }
    }

    @Test
    fun `Method apiResponseHandler  should execute error block when ApiResponse is Error`() {

        val apiResponse = Exception("Test").asError<List<Int>>()

        apiResponseHandler(
            status = apiResponse,
            onSuccess = { randomClass.onSuccess() },
            onError = { randomClass.onError() }
        )

        assertTrue(apiResponse is ApiResponse.Error)
        verify(exactly = 0) { randomClass.onSuccess() }
        verify(exactly = 1) { randomClass.onError() }
    }
}

interface RandomClass {
    suspend fun performAction(): List<Int>
    fun onSuccess()
    fun onError()
}