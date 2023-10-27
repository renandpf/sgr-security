CREATE TABLE `Usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cpf` varchar(11) NOT NULL,
  `senha` varchar(10) NOT NULL,
  `perfil` bigint NOT NULL,
  PRIMARY KEY (`id`)
)
