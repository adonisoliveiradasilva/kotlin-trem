package com.trem.application.adapter.out.persistence

import com.trem.application.domain.Assento
import com.trem.application.domain.Reserva
import com.trem.application.port.out.ReservaRepository

class MemoriaReservaAdapter : ReservaRepository {

    // Nosso "Banco de Dados" temporário. O vagão tem 5 assentos fixos.
    // Usamos 'listOf' porque a estrutura do vagão não muda, só o estado do assento.
    private val vagao = listOf(
        Assento("A1"), Assento("A2"), Assento("A3"), Assento("A4"), Assento("A5")
    )
    
    // Tabela de reservas. Usamos 'mutableListOf' porque vamos adicionar itens nela.
    private val bancoDeReservas = mutableListOf<Reserva>()

    // O compilador obriga o uso do 'override' para provar que estamos cumprindo o contrato
    override fun buscarAssento(numero: String): Assento? {
        // A função 'find' do Kotlin percorre a lista e devolve o primeiro que bater com a condição.
        // Se não achar nada, devolve nulo automaticamente.
        return vagao.find { it.numero == numero }
    }

    override fun salvarReserva(reserva: Reserva) {
        bancoDeReservas.add(reserva)
        // Um log falso para simular o banco de dados processando a informação
        println("\n[LOG DO BANCO] -> INSERINDO NO BD: Reserva salva para ${reserva.passageiro}")
    }
}