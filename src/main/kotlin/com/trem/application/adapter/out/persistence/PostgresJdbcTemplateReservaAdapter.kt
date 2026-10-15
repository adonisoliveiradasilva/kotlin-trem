package com.trem.application.adapter.out.persistence

import com.trem.application.domain.Assento
import com.trem.application.domain.Reserva
import com.trem.application.port.out.ReservaRepository
import javax.sql.DataSource

class PostgresJdbcTemplateReservaAdapter(
    private val dataSource: DataSource
) : ReservaRepository {

    override fun buscarAssento(numero: String): Assento? {
        val sql = "SELECT numero, ocupado FROM assentos WHERE numero = ?"

        return dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, numero)
                statement.executeQuery().use { rs ->
                    if (rs.next()) {
                        Assento(
                            numero = rs.getString("numero"),
                            ocupado = rs.getBoolean("ocupado")
                        )
                    } else {
                        null
                    }
                }
            }
        }
    }

    override fun salvarReserva(reserva: Reserva) {
        val sqlAtualizarAssento = "UPDATE assentos SET ocupado = true WHERE numero = ?"
        val sqlInserirReserva = "INSERT INTO reservas (passageiro, assento_numero) VALUES (?, ?)"

        dataSource.connection.use { connection ->
            connection.prepareStatement(sqlAtualizarAssento).use { statement ->
                statement.setString(1, reserva.assentoReservado.numero)
                statement.executeUpdate()
            }
            connection.prepareStatement(sqlInserirReserva).use { statement ->
                statement.setString(1, reserva.passageiro)
                statement.setString(2, reserva.assentoReservado.numero)
                statement.executeUpdate()
            }
        }

        println("\n[LOG DO JDBCTEMPLATE] -> Reserva salva no Postgres com código enxuto!")
    }
}