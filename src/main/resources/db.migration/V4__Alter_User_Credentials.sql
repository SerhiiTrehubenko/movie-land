ALTER TABLE IF EXISTS user_credentials
    ADD COLUMN IF NOT EXISTS role VARCHAR(20) CHECK (role IN ('ADMIN','USER'));

ALTER TABLE IF EXISTS user_credentials
    DROP COLUMN IF EXISTS role_id;

ALTER TABLE IF EXISTS user_credentials
    DROP COLUMN IF EXISTS sole;

DROP TABLE IF EXISTS roles;

DROP FUNCTION IF EXISTS get_pk_role_id();

CREATE VIEW user_with_credentials AS
SELECT u.user_id, u.user_firstname, u.user_lastname,
       u.user_email, u.user_nickname, uc.md5_password, uc.role
FROM users u, user_credentials uc
WHERE u.user_id = uc.user_id;

