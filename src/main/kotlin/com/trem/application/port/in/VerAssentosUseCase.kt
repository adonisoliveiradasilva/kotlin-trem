package com.trem.application.port.`in` // in é uma palavra reservada do Kotlin, então precisa ser colocada entre crases para não dar erro

import com.trem.application.domain.Assento

interface VerAssentosUseCase {
    fun verAssentos(): List<Assento>
}