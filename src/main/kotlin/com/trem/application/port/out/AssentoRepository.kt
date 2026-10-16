package com.trem.application.port.out

import com.trem.application.domain.Assento

interface AssentoRepository {
    fun listarAssentosDisponiveis(): List<Assento>
}