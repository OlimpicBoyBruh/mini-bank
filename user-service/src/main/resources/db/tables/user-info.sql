-- liquibase formatted sql
-- changeset danila.tatara:user-info-create

CREATE TABLE user_info (
  id UUID NOT NULL,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  birth_date DATE,
  email VARCHAR(255),
  phone_entered VARCHAR(30),
  phone_normalized VARCHAR(30),
  created_at TIMESTAMP(6),
  updated_at TIMESTAMP(6),
  PRIMARY KEY(id)
 );
