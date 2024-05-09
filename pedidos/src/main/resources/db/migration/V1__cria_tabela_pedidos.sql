CREATE TABLE pedidos (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  data_hora timestamp NOT NULL,
  status varchar(255) NOT NULL
)