CREATE FUNCTION  get_pk_role_id()
    RETURNS INTEGER
    LANGUAGE plpgsql
AS $$
BEGIN
    SELECT role_id FROM roles WHERE role_name LIKE 'USER';
END;
$$;


CREATE TABLE IF NOT EXISTS user_credentials (
    user_id INTEGER UNIQUE NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
            REFERENCES users(user_id),
    role_id INTEGER NOT NULL DEFAULT (get_pk_role_id()),
    CONSTRAINT fk_role
        FOREIGN KEY(role_id)
            REFERENCES roles(role_id),
    md5_password VARCHAR(100) NOT NULL UNIQUE,
    sole  VARCHAR(100) NOT NULL UNIQUE
);