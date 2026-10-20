package com.trem.application.adapter.`in`.web

import com.trem.application.port.`in`.ReservarAssentoUseCase
import com.trem.application.port.`in`.VerAssentosUseCase
import io.javalin.Javalin

data class ReservaRequest(val passageiro: String, val assento: String)

class TremRestController(
    private val reservarUseCase: ReservarAssentoUseCase,
    private val verAssentosUseCase: VerAssentosUseCase
) {
    fun registrarRotas(app: Javalin) {
        
        // ROTA 1: GET /assentos (Para listar assentos livres)
        app.get("/assentos") { contexto ->
            // Chama a porta de entrada (Caso de Uso)
            val assentos = verAssentosUseCase.verAssentos()
            
            // O Javalin converte a nossa lista do Kotlin para JSON automaticamente
            contexto.status(200).json(assentos)
        }

        // ROTA 2: POST /reserva (Para comprar a passagem)
        app.post("/reserva") { contexto ->
            try {
                // Pega o corpo do JSON que veio do Insomnia e transforma no DTO
                val request = contexto.bodyAsClass(ReservaRequest::class.java)

                // Chama a porta de entrada (Caso de Uso)
                val reserva = reservarUseCase.reservar(request.passageiro, request.assento)
                
                // Devolve a reserva confirmada em JSON com o status HTTP 201 (Created)
                contexto.status(201).json(reserva)

            } catch (e: Exception) {
                // Se o Domínio recusar a reserva (Exception), devolvemos erro 400 (Bad Request)
                contexto.status(400).result(e.message ?: "Erro desconhecido")
            }
        }
    }
}