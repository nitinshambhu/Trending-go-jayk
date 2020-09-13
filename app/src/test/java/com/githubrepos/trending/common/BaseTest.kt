package com.githubrepos.trending.common

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before

abstract class BaseTest {

    @Before
    open fun setUp() = MockKAnnotations.init(this)

    @After
    open fun tearDown() = unmockkAll()
}