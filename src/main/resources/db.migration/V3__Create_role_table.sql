CREATE SEQUENCE IF NOT EXISTS roles_id
    AS INT
    INCREMENT 1
    MINVALUE 1001
    MAXVALUE 1100
    START 1001
    NO CYCLE;

CREATE TABLE IF NOT EXISTS roles (
  role_id INTEGER PRIMARY KEY NOT NULL UNIQUE DEFAULT nextval('roles_id') CHECK (role_id BETWEEN 1001 AND 1100),
  role_name VARCHAR(50) NOT NULL UNIQUE
);

ALTER SEQUENCE roles_id
    OWNED BY roles.role_id;

CREATE FUNCTION  forbid_update_role_id()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.role_id != OLD.role_id OR
       NEW.role_id < 1001 OR
       NEW.role_id > 1100 THEN
        RAISE EXCEPTION 'update forbidden';
    END IF;
END;
$$;

CREATE TRIGGER role_id_update
    BEFORE UPDATE
    ON roles
    FOR EACH ROW
EXECUTE PROCEDURE forbid_update_role_id();

INSERT INTO roles
(role_name)
VALUES
('USER'),
('ADMIN');