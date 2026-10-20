package com.trem.application.port.out

import com.trem.application.domain.Assento
import com.trem.application.domain.Reserva

interface ReservaRepository {
    fun buscarAssento(numero: String): Assento?
    fun salvarReserva(reserva: Reserva)
}