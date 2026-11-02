-- Script SQL opcional para instâncias externas (PostgreSQL / MySQL).
-- O Spring Boot com JPA e H2 gera e atualiza a estrutura de tabelas automaticamente via ddl-auto=update.

CREATE TABLE IF NOT EXISTS tb_historico_chat (
    id BIGSERIAL PRIMARY KEY,
    pergunta TEXT NOT NULL,
    resposta TEXT NOT NULL,
    provedor VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    data_hora TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_historico_data_hora ON tb_historico_chat (data_hora DESC);
