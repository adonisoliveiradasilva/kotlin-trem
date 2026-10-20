package com.trem.application.domain

import com.trem.application.port.`in`.ReservarAssentoUseCase
import com.trem.application.port.out.ReservaRepository

class ReservarAssentoService(
    private val reservaRepository: ReservaRepository
) : ReservarAssentoUseCase {

    override fun reservar(passageiro: String, numeroAssento: String): Reserva {
        
        // 1. Busca a cadeira no mundo externo (Porta de Saída)
        val assento = reservaRepository.buscarAssento(numeroAssento)
            ?: throw IllegalArgumentException("Erro: Assento $numeroAssento não existe no vagão.")

        // 3. Regra de Negócio: O assento está livre?
        if (assento.ocupado) {
            throw IllegalStateException("Erro: Assento $numeroAssento já está ocupado por outra pessoa.")
        }

        // 4. Se chegou aqui, as regras passaram. Atualiza o estado da entidade.
        assento.ocupado = true
        val novaReserva = Reserva(passageiro, assento)

        // 5. Manda o mundo externo salvar os dados (Porta de Saída)
        reservaRepository.salvarReserva(novaReserva)

        return novaReserva
    }
}