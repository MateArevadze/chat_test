CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS messages
(
    id                     VARCHAR(60) DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id                VARCHAR(60)  NOT NULL,
    game_id                VARCHAR(60)  NOT NULL,
    content                VARCHAR      NOT NULL,
    time_sent              TIMESTAMP    NOT NULL
);

