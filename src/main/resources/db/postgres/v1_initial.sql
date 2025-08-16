--liquibase formatted sql

--changeset initial:1

-- Enable pgvector for embeddings
CREATE
EXTENSION IF NOT EXISTS vector;

-- Meanings table: core semantic units
CREATE TABLE meanings
(
    id            BIGSERIAL PRIMARY KEY,
    content       TEXT        NOT NULL,
    sources       JSONB       NOT NULL DEFAULT '[]', -- list of sources
    metadata      JSONB       NOT NULL DEFAULT '{}', -- extra info
    embedding     vector(1536),                      -- semantic embedding
    model_version VARCHAR(32) NOT NULL,
    created_at    TIMESTAMPTZ          DEFAULT now(),
    updated_at    TIMESTAMPTZ          DEFAULT now()
);

-- Index for vector search
CREATE INDEX meanings_embedding_idx
    ON meanings USING ivfflat (embedding vector_l2_ops) WITH (lists = 100);

-- Memories table: persistent but limited working memory
CREATE TABLE memories
(
    id               BIGSERIAL PRIMARY KEY,
    kind             VARCHAR(32) NOT NULL,                     -- e.g. 'fact', 'event', 'plan', 'system', 'user_note'
    content          TEXT        NOT NULL,
    related_meanings BIGINT[] NOT NULL DEFAULT '{}'::BIGINT[], -- array of meaning IDs
    weight           REAL        NOT NULL DEFAULT 1.0,
    last_accessed    TIMESTAMPTZ          DEFAULT now(),
    times_seen       INTEGER     NOT NULL DEFAULT 0,
    model_version    VARCHAR(32) NOT NULL,
    created_at       TIMESTAMPTZ          DEFAULT now(),
    updated_at       TIMESTAMPTZ          DEFAULT now(),
    FOREIGN KEY (related_meanings) REFERENCES meanings (id) ON DELETE SET NULL
);

-- Dialog logs: one row per conversation/session
CREATE TABLE dialogs
(
    id            BIGSERIAL PRIMARY KEY,
    session_id    VARCHAR(255) NOT NULL,
    started_at    TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    ended_at      TIMESTAMPTZ,
    model_version VARCHAR(32)  NOT NULL,
    summary       JSONB      DEFAULT '{}'::jsonb,
    metadata      JSONB      DEFAULT '{}'::jsonb
);

-- Dialog messages: one row per message
CREATE TABLE messages
(
    id         BIGSERIAL PRIMARY KEY,
    dialog_id  BIGINT      NOT NULL REFERENCES dialogs (id) ON DELETE CASCADE,
    role       VARCHAR(32) NOT NULL,
    content    TEXT        NOT NULL,
    metadata   JSONB      DEFAULT '{}'::jsonb,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_messages_dialog_id ON dialogs(dialog_id);

-- Model actions: changes applied by model or user
CREATE TABLE model_actions
(
    id            BIGSERIAL PRIMARY KEY,
    action_type   TEXT        NOT NULL,
    payload       JSONB       NOT NULL,
    applied_by    VARCHAR(32) NOT NULL, -- 'system', 'user', 'auto'
    model_version VARCHAR(32) NOT NULL,
    created_at    TIMESTAMPTZ DEFAULT now()
);
