package com.trem.application.domain

import com.trem.application.port.`in`.VerAssentosUseCase
import com.trem.application.port.out.AssentoRepository

class VerAssentosService(
    private val assentoRepository: AssentoRepository
) : VerAssentosUseCase {

    override fun verAssentos(): List<Assento> {
        
        // 1. Busca as cadeira no mundo externo (Porta de Saída)
        val assentos = assentoRepository.verAssentos()
            ?: throw IllegalArgumentException("Erro: Não foi encontrado nenhum assento.")

        return assentos
    }
}