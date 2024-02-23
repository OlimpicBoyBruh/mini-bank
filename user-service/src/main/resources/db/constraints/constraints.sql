-- liquibase formatted sql
-- changeset danila.tatara:constraints-create

ALTER TABLE IF EXISTS user_password ADD CONSTRAINT user_password_user_info_fk
  FOREIGN KEY (id) REFERENCES user_info;
