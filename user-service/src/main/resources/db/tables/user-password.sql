-- liquibase formatted sql
-- changeset danila.tatara:user-password-create

CREATE TABLE user_password (
  id UUID NOT NULL,
  password VARCHAR(255),
  PRIMARY KEY(id)
);
