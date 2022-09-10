--liquibase formatted sql
--changeset pacion:3
INSERT into category (name, created, updated) VALUES ('test', null, null);
INSERT INTO role (name) VALUES ('testRole');
INSERT INTO app_user (username, email, password, first_name, second_name, created, updated) VALUES ('test', 'test@wp.pl', 'test', '{bcrypt}test', 'test', null, null);
INSERT INTO user_roles (role_id, user_id) VALUES(1, 1);
INSERT INTO transaction (transaction_type, value, quantity, title, description, category_id, appuser_id, created, updated) VALUES ('INCOME', 12, 14, 're', 're', 1, 1, null, null)