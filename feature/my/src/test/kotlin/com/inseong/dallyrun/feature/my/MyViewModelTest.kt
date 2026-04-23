package com.inseong.dallyrun.feature.my

import com.inseong.dallyrun.core.testing.MainDispatcherRule
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class MyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should keep initial state on creation`() {
        val vm = MyViewModel()
        assertFalse(vm.uiState.value.isLoading)
    }
}
