package com.trem.application.adapter.out.persistence

import com.trem.application.domain.Assento
import com.trem.application.domain.Reserva
import com.trem.application.port.out.ReservaRepository
import java.sql.Connection
import java.sql.DriverManager

class PostgresReservaAdapter(
    private val url: String,
    private val usuario: String,
    private val senha: String
) : ReservaRepository {

    // Função auxiliar privada que abre a conexão com o Postgres
    private fun obterConexao(): Connection {
        return DriverManager.getConnection(url, usuario, senha)
    }   

    override fun buscarAssento(numero: String): Assento? {
        val sql = "SELECT numero, ocupado FROM assentos WHERE numero = ?"

        // O '.use' do Kotlin fecha a conexão automaticamente no final!
        obterConexao().use { conexao ->
            conexao.prepareStatement(sql).use { stmt ->
                // Substitui o '?' da query pelo número do assento com segurança
                stmt.setString(1, numero)

                stmt.executeQuery().use { resultado ->
                    // Se o banco encontrou a linha, transformamos em objeto de Domínio
                    if (resultado.next()) {
                        return Assento(
                            numero = resultado.getString("numero"),
                            ocupado = resultado.getBoolean("ocupado")
                        )
                    }
                }
            }
        }
        // Se não encontrou nenhuma linha no banco, retorna nulo
        return null
    }

    override fun salvarReserva(reserva: Reserva) {
        val sqlAtualizarAssento = "UPDATE assentos SET ocupado = true WHERE numero = ?"
        val sqlInserirReserva = "INSERT INTO reservas (passageiro, assento_numero) VALUES (?, ?)"

        obterConexao().use { conexao ->
            // Desligamos o salvamento automático para criar uma TRANSAÇÃO segura
            conexao.autoCommit = false

            try {
                // 1. Marca o assento como ocupado na tabela 'assentos'
                conexao.prepareStatement(sqlAtualizarAssento).use { stmtAssento ->
                    stmtAssento.setString(1, reserva.assentoReservado.numero)
                    stmtAssento.executeUpdate()
                }

                // 2. Cria o registro da reserva na tabela 'reservas'
                conexao.prepareStatement(sqlInserirReserva).use { stmtReserva ->
                    stmtReserva.setString(1, reserva.passageiro)
                    stmtReserva.setString(2, reserva.assentoReservado.numero)
                    stmtReserva.executeUpdate()
                }

                // 3. Confirma as duas operações juntas no banco!
                conexao.commit()
                println("\n[LOG DO POSTGRES] -> Transação concluída! Reserva salva no banco de dados.")
            } catch (e: Exception) {
                // Se qualquer erro acontecer no meio, desfaz tudo para não corromper os dados
                conexao.rollback()
                throw e
            }
        }
    }
}