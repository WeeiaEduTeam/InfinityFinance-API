--liquibase formatted sql
--changeset pacion:4
CREATE TABLE transaction
(
    transaction_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    transaction_type VARCHAR(255),
    value            INTEGER                                 NOT NULL,
    quantity         INTEGER                                 NOT NULL,
    title            VARCHAR(255),
    description      VARCHAR(255),
    category_id      BIGINT,
    appuser_id       BIGINT,
    created          TIMESTAMP WITHOUT TIME ZONE,
    updated          TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_transaction PRIMARY KEY (transaction_id)
);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_APPUSER FOREIGN KEY (appuser_id) REFERENCES app_user (user_id);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (category_id);