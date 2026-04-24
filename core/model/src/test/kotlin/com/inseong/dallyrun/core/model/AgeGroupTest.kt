package com.inseong.dallyrun.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AgeGroupTest {

    @Test
    fun `serverValue should map to decade number`() {
        assertEquals(20, AgeGroup.TWENTIES.serverValue)
        assertEquals(30, AgeGroup.THIRTIES.serverValue)
        assertEquals(40, AgeGroup.FORTIES.serverValue)
        assertEquals(50, AgeGroup.FIFTIES.serverValue)
        assertEquals(60, AgeGroup.SIXTIES_OR_OVER.serverValue)
    }

    @Test
    fun `should have exactly 5 entries`() {
        assertEquals(5, AgeGroup.entries.size)
    }
}
