package com.chub.petsafebrands.domain

import com.chub.petsafebrands.domain.usecases.MultiplicationUseCase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class MultiplicationUseCaseTest {

    @Test
    fun multiplication() {
        val useCase = MultiplicationUseCase()
        val result = useCase("100", BigDecimal(0.5))
        assertThat(result, Matchers.comparesEqualTo(BigDecimal("50")))
    }

    @Test
    fun `multiplication - invalid base amount`() {
        val useCase = MultiplicationUseCase()
        val result = useCase("--23--", BigDecimal(0.5))
        Assertions.assertEquals(BigDecimal.ZERO, result)
    }
}