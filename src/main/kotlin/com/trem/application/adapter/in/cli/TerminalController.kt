package com.trem.application.adapter.`in`.cli

import com.trem.application.port.`in`.ReservarAssentoUseCase
import java.util.Scanner

class TerminalController(
    // A Injeção de Dependência! O Controller não conhece a classe Service,
    // ele conhece apenas o contrato da Porta de Entrada.
    private val useCase: ReservarAssentoUseCase
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

            println("Qual assento você deseja? (Ex: A1, A2, A3, A4, A5):")
            val assentoStr = scanner.nextLine()

            // O Controller tenta executar a ação. Se o Domínio recusar a regra,
            // o bloco catch captura a Exception e mostra o erro de forma amigável.
            try {
                // Chamamos a Porta de Entrada, passando apenas tipos primitivos (String)
                val reserva = useCase.reservar(nome, assentoStr.uppercase())
                println("\n✅ SUCESSO! Passagem emitida para ${reserva.passageiro} no assento${reserva.assentoReservado.numero}.")
            } catch (e: Exception) {
                println("\n❌ FALHA NA RESERVA: ${e.message}")
            }
        }
    }
}