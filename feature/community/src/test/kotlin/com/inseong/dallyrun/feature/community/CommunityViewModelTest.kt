package com.inseong.dallyrun.feature.community

import com.inseong.dallyrun.core.testing.MainDispatcherRule
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class CommunityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should keep initial state on creation`() {
        val vm = CommunityViewModel()
        assertFalse(vm.uiState.value.isLoading)
    }
}
