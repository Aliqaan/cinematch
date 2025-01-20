CREATE SCHEMA IF NOT EXISTS cinematch;

CREATE TABLE IF NOT EXISTS cinematch.users
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(255)                           NOT NULL,
    password   VARCHAR(255)                           NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);