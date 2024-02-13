-- liquibase formatted sql
-- changeset danila.tatara:user-info-create

CREATE TABLE user_info (
  id UUID NOT NULL,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  birth_date DATE,
  email VARCHAR(255),
  phone VARCHAR(20),
  created_at TIMESTAMP(6),
  updated_at TIMESTAMP(6),
  PRIMARY KEY(id)
 );
