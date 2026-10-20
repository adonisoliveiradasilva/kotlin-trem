package com.trem.application.domain

import com.trem.application.port.`in`.VerAssentosUseCase
import com.trem.application.port.out.AssentoRepository

class VerAssentoService(
    private val assentoRepository: AssentoRepository
) : VerAssentosUseCase {

    override fun verAssentosDisponiveis(): List<Assento> {
        val assentos = assentoRepository.listarAssentosDisponiveis()

        // Regra de Negócio: e se o vagão inteiro já estiver vendido?
        if (assentos.isEmpty()) {
            throw IllegalStateException("Que pena! Todos os assentos do trem já estão ocupados.")
        }

        return assentos
    }
}