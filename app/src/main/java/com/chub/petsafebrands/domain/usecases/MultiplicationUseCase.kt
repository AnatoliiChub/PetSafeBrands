package com.chub.petsafebrands.domain.usecases

import com.chub.petsafebrands.isValidAmountOfMoney
import java.math.BigDecimal
import javax.inject.Inject

class MultiplicationUseCase @Inject constructor() {

    operator fun invoke(baseAmount: String, coefficient: BigDecimal): BigDecimal {
        return if (baseAmount.isValidAmountOfMoney()) {
            coefficient * BigDecimal(baseAmount)
        } else {
            BigDecimal.ZERO
        }
    }
}