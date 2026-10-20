package com.trem.application.adapter.out.persistence

import com.trem.application.domain.Assento
import com.trem.application.domain.Reserva
import com.trem.application.port.out.AssentoRepository
import com.trem.application.port.out.ReservaRepository
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

// Olhe a mágica aqui: a MESMA classe implementa as DUAS Portas de Saída!
class PostgresJdbcTemplateReservaAdapter(
    dataSource: DataSource
) : ReservaRepository, AssentoRepository {

    // Instanciamos a ferramenta do Spring usando o seu dataSource!

    private val jdbcTemplate = JdbcTemplate(dataSource)

    // 1. Método exigido pelo contrato AssentoRepository:
    override fun listarAssentosDisponiveis(): List<Assento> {
        val sql = "SELECT numero, ocupado FROM assentos WHERE ocupado = false ORDER BY numero"
        return jdbcTemplate.query(sql) { rs, _ ->
            Assento(
                numero = rs.getString("numero"),
                ocupado = rs.getBoolean("ocupado")
            )
        }
    }

    // 2. Métodos que já existiam do contrato ReservaRepository:
    override fun buscarAssento(numero: String): Assento? {
        val sql = "SELECT numero, ocupado FROM assentos WHERE numero = ?"
        return jdbcTemplate.query(sql, { rs, _ ->
            Assento(
                numero = rs.getString("numero"),
                ocupado = rs.getBoolean("ocupado")
            )
        }, numero).firstOrNull()
    }

    override fun verAssentos(): List<Assento> {
        // SQL corrigido com o WHERE!
        val sql = "SELECT numero, ocupado FROM assentos ORDER BY numero"
        
        return jdbcTemplate.query(sql) { rs, _ ->
            Assento(
                numero = rs.getString("numero"),
                ocupado = rs.getBoolean("ocupado")
            )
        }
    }

    override fun salvarReserva(reserva: Reserva) {
        val sqlAtualizarAssento = "UPDATE assentos SET ocupado = true WHERE numero = ?"
        val sqlInserirReserva = "INSERT INTO reservas (passageiro, assento_numero) VALUES (?, ?)"

        jdbcTemplate.update(sqlAtualizarAssento, reserva.assentoReservado.numero)
        jdbcTemplate.update(sqlInserirReserva, reserva.passageiro, reserva.assentoReservado.numero)
        
        println("\n[LOG DO JDBCTEMPLATE] -> Reserva salva no Postgres com código enxuto!")
    }
}