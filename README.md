# 🚂 Trem Turístico Ouro Preto - Mariana | Arquitetura Hexagonal em Kotlin

Este projeto é uma Prova de Conceito (POC) desenvolvida em **Kotlin** para demonstrar na prática a aplicação da **Arquitetura Hexagonal (Ports & Adapters)**. 

O objetivo principal da aplicação é gerenciar a reserva de assentos de um vagão turístico na rota fixa entre Ouro Preto e Mariana, garantindo que **100% das regras de negócio (Domínio) estejam isoladas de frameworks, bancos de dados e interfaces de usuário**.

---

## 🎯 Destaques Arquiteturais (Por que este projeto existe?)

Em sistemas de alta escala, acoplar regras de negócio diretamente a ORMs ou frameworks web dificulta testes automatizados e mudanças tecnológicas. Este repositório demonstra como resolver esse problema aplicando:

1. **Isolamento Total do Core (`application/domain`):** As entidades e serviços de domínio são escritos em Kotlin puro, sem nenhuma dependência externa.
2. **Inversão de Dependência com Portas (`port/in` e `port/out`):** A comunicação entre o domínio e o mundo externo ocorre estritamente através de contratos (Interfaces).
3. **Intercambialidade de Adaptadores de Persistência (`adapter/out/persistence`):** A mesma porta de saída (`ReservaRepository`) possui **3 implementações distintas** que podem ser alternadas alterando apenas uma linha na inicialização (`App.kt`):
   - 🧠 **`MemoriaReservaAdapter`**: Persistência em memória utilizando coleções nativas do Kotlin (ideal para testes rápidos de fluxo).
   - 🐘 **`PostgresReservaAdapter` (JDBC Puro)**: Implementação com `DriverManager` e `PreparedStatement`, demonstrando controle manual de transações (`autoCommit = false`, `commit` e `rollback`), prevenção contra *SQL Injection* e fechamento seguro de conexões com `.use {}`.
   - ⚡ **`PostgresJdbcTemplateReservaAdapter` (Spring JdbcTemplate)**: Implementação enxuta com `JdbcTemplate` e *RowMappers* via expressões Lambda, mantendo controle total do SQL sem a sobrecarga de um ORM (como Hibernate/JPA) e eliminando código repetitivo (*boilerplate*).

---

## 📂 Estrutura de Diretórios

```text
src/main/kotlin/com/trem/
├── App.kt                                             # Ponto de entrada e injeção manual de dependências
├── application/                                       # CORAÇÃO DO SISTEMA (Isolado da infraestrutura)
│   ├── domain/
│   │   ├── Assento.kt                                 # Entidade representando a cadeira física
│   │   ├── Reserva.kt                                 # Entidade representando o bilhete emitido
│   │   └── ReservarAssentoService.kt                  # Caso de Uso com as regras de negócio puras
│   └── port/
│       ├── in/
│       │   └── ReservarAssentoUseCase.kt              # Porta de Entrada (Input Port)
│       └── out/
│           └── ReservaRepository.kt                   # Porta de Saída (Output Port)
└── adapter/                                           # MUNDO EXTERNO (Detalhes técnicos e traduções)
    ├── in/
    │   └── cli/
    │       └── TerminalController.kt                  # Adaptador de Entrada via linha de comando (CLI)
    └── out/
        └── persistence/
            ├── MemoriaReservaAdapter.kt               # Adaptador 1: Banco em memória
            ├── PostgresReservaAdapter.kt              # Adaptador 2: PostgreSQL com JDBC Puro
            └── PostgresJdbcTemplateReservaAdapter.kt  # Adaptador 3: PostgreSQL com Spring JdbcTemplate
```

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Kotlin (JVM / JDK 21)
- **Build System:** Gradle (Kotlin DSL)
- **Banco de Dados:** PostgreSQL (executando via Docker)
- **Acesso a Dados:** JDBC Nativo (`org.postgresql:postgresql`) e Spring JDBC (`org.springframework:spring-jdbc`)

---

## 🚀 Como Executar o Projeto

### 1. Pré-requisitos
- **Java JDK 21** instalado.
- **Docker** instalado e em execução (necessário para os adaptadores PostgreSQL).

### 2. Subindo o Banco de Dados PostgreSQL (Docker)
Execute os comandos abaixo no terminal para iniciar o container do PostgreSQL na porta `5433` e criar a estrutura inicial de tabelas e assentos (`A1` a `A5`):

```bash
# 1. Inicia o container PostgreSQL
docker run --name postgres-trem -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=trem_db -p 5433:5432 -d postgres

# 2. Aguarde 5 segundos e crie as tabelas populando os 5 assentos do vagão
docker exec -i postgres-trem psql -U postgres -d trem_db -c "
CREATE TABLE assentos (numero VARCHAR(10) PRIMARY KEY, ocupado BOOLEAN NOT NULL DEFAULT FALSE);
CREATE TABLE reservas (id SERIAL PRIMARY KEY, passageiro VARCHAR(100) NOT NULL, assento_numero VARCHAR(10) NOT NULL REFERENCES assentos(numero));
INSERT INTO assentos (numero, ocupado) VALUES ('A1', false), ('A2', false), ('A3', false), ('A4', false), ('A5', false);
"
```

### 3. Executando a Aplicação

No **Linux / macOS**:
```bash
./gradlew run -q --console=plain
```

No **Windows (PowerShell)**:
```powershell
.\gradlew.bat run -q --console=plain
```

---

## 🧪 Regras de Negócio Implementadas

1. **Validação de Existência:** Caso o usuário solicite um assento que não pertence ao vagão (ex: `X9`), o domínio rejeita a operação com `IllegalArgumentException`.
2. **Validação de Disponibilidade:** Caso o assento solicitado já esteja reservado, o domínio bloqueia a duplicidade com `IllegalStateException`.
3. **Persistência Transacional:** Ao confirmar a reserva no PostgreSQL, o status do assento é atualizado para `ocupado = true` e o registro do passageiro é salvo na tabela `reservas`.

---

## 👨‍💻 Autor

Desenvolvido por **Adonis Oliveira da Silva** como estudo prático de Arquitetura de Software e Engenharia Backend em Kotlin.