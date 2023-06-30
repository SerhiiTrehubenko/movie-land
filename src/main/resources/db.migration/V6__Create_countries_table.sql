CREATE SEQUENCE IF NOT EXISTS countries_id
    AS INT
    INCREMENT 1
    MINVALUE 501
    MAXVALUE 1000
    START 501
    NO CYCLE;

CREATE TABLE IF NOT EXISTS countries (
  country_id INTEGER PRIMARY KEY NOT NULL UNIQUE DEFAULT nextval('countries_id') CHECK (country_id BETWEEN 501 AND 1000),
  country_name VARCHAR(100) NOT NULL UNIQUE
);

ALTER SEQUENCE countries_id
    OWNED BY countries.country_id;

CREATE FUNCTION  forbid_update_country_id()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.country_id != OLD.country_id OR
       NEW.country_id < 501 OR
       NEW.country_id > 1000 THEN
        RAISE EXCEPTION 'update forbidden';
    END IF;
END;
$$;

CREATE TRIGGER country_id_update
    BEFORE UPDATE
    ON countries
    FOR EACH ROW
EXECUTE PROCEDURE forbid_update_country_id();