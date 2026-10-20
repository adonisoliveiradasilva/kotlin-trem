package com.trem

import com.trem.application.adapter.`in`.cli.TerminalController
import com.trem.application.adapter.`in`.web.TremRestController
import com.trem.application.adapter.out.persistence.MemoriaReservaAdapter
import com.trem.application.adapter.out.persistence.PostgresJdbcTemplateReservaAdapter
import com.trem.application.adapter.out.persistence.PostgresReservaAdapter
import com.trem.application.domain.ReservarAssentoService
import com.trem.application.domain.VerAssentosService
import io.javalin.Javalin
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
    val url = "jdbc:postgresql://localhost:5433/trem_db"
    val usuario = "postgres"
    val senha = "1234"

    // 1. Adaptador de Saída (Banco de Dados)
    val dataSource = DriverManagerDataSource(url, usuario, senha)
    val repositorio = PostgresJdbcTemplateReservaAdapter(dataSource)
    val servicoReserva = ReservarAssentoService(repositorio)
    val servicoVerAssentos = VerAssentosService(repositorio)

    val app = Javalin.create().start(8080)
    val webController = TremRestController(servicoReserva, servicoVerAssentos)
    webController.registrarRotas(app)
 
   println("🚀 API rodando! Teste no Insomnia em: http://localhost:8080")
}