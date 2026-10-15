package com.trem

import com.trem.application.adapter.`in`.cli.TerminalController
import com.trem.application.adapter.out.persistence.MemoriaReservaAdapter
import com.trem.application.adapter.out.persistence.PostgresJdbcTemplateReservaAdapter
import com.trem.application.adapter.out.persistence.PostgresReservaAdapter
import com.trem.application.domain.ReservarAssentoService
import org.springframework.jdbc.datasource.DriverManagerDataSource

fun main() {
    // =========================================================================
    // LABORATÓRIO DE ARQUITETURA HEXAGONAL: ESCOLHA SEU ADAPTADOR DE SAÍDA!
    // =========================================================================

    // OPÇÃO 1: Banco em Memória
    // val repositorio = MemoriaReservaAdapter()

    // OPÇÃO 2: Postgres com JDBC Puro
    // val repositorio = PostgresReservaAdapter(
    //     url = "jdbc:postgresql://localhost:5433/trem_db",
    //     usuario = "postgres",
    //     senha = "1234"
    // )

    // OPÇÃO 3: Postgres com Spring JdbcTemplate (ATIVA AGORA!)
    val dataSource = DriverManagerDataSource(
        "jdbc:postgresql://localhost:5433/trem_db",
        "postgres",
        "1234"
    )
    val repositorio = PostgresJdbcTemplateReservaAdapter(dataSource)

    // =========================================================================
    // O RESTO DO SISTEMA NÃO MUDA NADA!
    // =========================================================================
    val servico = ReservarAssentoService(repositorio)
    val terminal = TerminalController(servico)

    terminal.iniciar()
}