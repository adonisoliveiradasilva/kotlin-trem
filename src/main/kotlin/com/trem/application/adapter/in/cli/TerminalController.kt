package com.trem.application.adapter.`in`.cli

import com.trem.application.port.`in`.ReservarAssentoUseCase
import com.trem.application.port.`in`.VerAssentosUseCase
import java.util.Scanner

class TerminalController(
    // Agora o Controller recebe as DUAS Portas de Entrada!
    private val reservarUseCase: ReservarAssentoUseCase,
    private val verAssentosUseCase: VerAssentosUseCase
) {
    fun iniciar() {
        val scanner = Scanner(System.`in`)
        println("=== BEM-VINDO AO TREM OURO PRETO-MARIANA ===")

        while (true) {
            println("\nDigite o seu nome (ou 'sair' para encerrar):")
            val nome = scanner.nextLine()
            if (nome.lowercase() == "sair") {
                println("Encerrando o sistema. Até logo!")
                break
            }

            try {
                // 1. Chama o novo Caso de Uso para listar os assentos disponíveis
                val assentos = verAssentosUseCase.verAssentos()

                println("\nAssentos disponíveis no momento:")
                for (assento in assentos) {
                    println("-> ${assento.numero}")
                }

                // 2. Pergunta qual assento o passageiro quer
                println("\nQual assento você deseja reservar?")
                val assentoStr = scanner.nextLine()

                // 3. Chama o Caso de Uso de reserva
                val reserva = reservarUseCase.reservar(nome, assentoStr.uppercase())
                println("\n✅ SUCESSO! Passagem emitida para ${reserva.passageiro} no assento${reserva.assentoReservado.numero}.")
            } catch (e: Exception) {
                println("\n❌ AVISO: ${e.message}")
            }
        }
    }
}