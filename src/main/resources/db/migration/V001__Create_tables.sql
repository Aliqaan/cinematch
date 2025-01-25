CREATE SCHEMA IF NOT EXISTS cinematch;

-- Grant USAGE on the schema to postgres
GRANT USAGE ON SCHEMA cinematch TO postgres;

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA cinematch GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO postgres;


CREATE TABLE cinematch.users
(
    id            SERIAL PRIMARY KEY,                     -- Unique user ID
    email         VARCHAR(255) UNIQUE NOT NULL,           -- User email (used for login)
    first_name    VARCHAR(63)         NOT NULL,           -- First name
    last_name     VARCHAR(63)         NOT NULL,           -- Last name
    password_hash TEXT                NOT NULL,           -- Hashed password for security
    status        VARCHAR(50)         NOT NULL,           -- User status
    last_login    TIMESTAMP WITH TIME ZONE,               -- Last login timestamp
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT NOW(), -- Account creation date
    updated_at    TIMESTAMP WITH TIME ZONE,               -- Last update timestamp
    deleted_at    TIMESTAMP WITH TIME ZONE                -- Account deletion timestamp
);


CREATE TABLE cinematch.user_tokens
(
    id         SERIAL PRIMARY KEY,                     -- Unique session ID
    user_id    INT                      NOT NULL
        REFERENCES cinematch.users (id),               -- Link to the user
    token      TEXT UNIQUE              NOT NULL,      -- Token
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(), -- Token creation date
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL       -- Expiry for the token
);