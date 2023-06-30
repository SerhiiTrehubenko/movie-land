CREATE SEQUENCE IF NOT EXISTS users_id
    AS INT
    INCREMENT 1
    MINVALUE 1000001
    START 1000001
    NO CYCLE;

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY NOT NULL UNIQUE DEFAULT nextval('users_id') CHECK (user_id >= 1000001),
    user_firstname VARCHAR(100) NOT NULL,
    user_lastname VARCHAR(100) NOT NULL,
    user_nickname VARCHAR(100) NOT NULL UNIQUE,
    user_email VARCHAR(100) NOT NULL UNIQUE,
    user_record_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER SEQUENCE users_id
    OWNED BY users.user_id;

CREATE FUNCTION  forbid_update_user_id()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.user_id <> OLD.user_id OR
       NEW.user_id < 1000001
       THEN
        RAISE EXCEPTION 'update forbidden';
    END IF;
END;
$$;

CREATE TRIGGER user_id_update
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE forbid_update_user_id();

INSERT INTO users (user_firstname, user_lastname, user_nickname, user_email)
VALUES ('ADMIN', 'ADMIN', 'ADMIN', 'admin@admin.com');