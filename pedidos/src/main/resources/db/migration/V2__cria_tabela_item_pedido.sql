CREATE TABLE item_do_pedido (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  descricao varchar(255) DEFAULT NULL,
  quantidade numeric(11) NOT NULL,
  pedido_id BIGSERIAL NOT NULL,
  CONSTRAINT pedido_fk FOREIGN KEY (pedido_id) REFERENCES pedidos(id)
)