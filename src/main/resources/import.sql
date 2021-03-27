insert into cozinha (id, nome) values (1, 'Brasileira');
insert into cozinha (id, nome) values (2, 'Japonesa');
insert into cozinha (id, nome) values (3, 'Doces e Bolos');
insert into cozinha (id, nome) values (4, 'Tailandesa');
insert into cozinha (id, nome) values (5, 'Arabe');
insert into cozinha (id, nome) values (6, 'Marroquina');

insert into restaurante (id, nome, taxa_frete) values (1, 'Maria Fumaca', 5.00);
insert into restaurante (id, nome, taxa_frete) values (2, 'Cacau Fit', 6.00);
insert into restaurante (id, nome, taxa_frete) values (3, 'kyatai', 9.99);
insert into restaurante (id, nome, taxa_frete) values (4, 'Divino Fogao e Lenha', 7.00);
insert into restaurante (id, nome, taxa_frete) values (5, 'Maria Brasileira', 3.00);


insert into restaurante_cozinha values (3, 2);
insert into restaurante_cozinha values (3, 4);
insert into restaurante_cozinha values (1, 1);
insert into restaurante_cozinha values (1, 3);
insert into restaurante_cozinha values (4, 5);

INSERT INTO `tb_estados` VALUES ('AC', 'Acre');
INSERT INTO `tb_estados` VALUES ('MG', 'Minas Gerais');
INSERT INTO `tb_estados` VALUES ('SP', 'Sao Paulo');

INSERT INTO `tb_cidades` (id, estado_sigla, nome) VALUES (0001, 'AC', 'Acrelandia');
INSERT INTO `tb_cidades` (id, estado_sigla, nome) VALUES (0002, 'AC', 'Assis Brasil');
INSERT INTO `tb_cidades` (id, estado_sigla, nome) VALUES (9314, 'SP', 'Ribeirao Preto');
INSERT INTO `tb_cidades` (id, estado_sigla, nome) VALUES (9413, 'SP', 'Sao Jose do Rio Preto');
INSERT INTO `tb_cidades` (id, estado_sigla, nome) VALUES (9422, 'SP', 'Sao Paulo');
INSERT INTO `tb_cidades` (id, estado_sigla, nome) VALUES (3988, 'MG', 'Uberlandia');
INSERT INTO `tb_cidades` (id, estado_sigla, nome) VALUES (3088, 'MG', 'Frutal');

INSERT INTO `forma_pagamento` VALUES (1, 'Cartão de Crédito');
INSERT INTO `forma_pagamento` VALUES (2, 'Cartão de Débito');
INSERT INTO `forma_pagamento` VALUES (3, 'Vale Refeição');
INSERT INTO `forma_pagamento` VALUES (4, 'Dinheiro');

insert into endereco values ("rua Jose Luis de Mendonca", "270", "Lago Sul", "15115000", "Casa", 9413);
insert into endereco values ("rua Penia", "2835", "Centro", "15010100", "Casa", 9413);
insert into endereco values ("rua Pirajuba", "1401", "Ipe Amarelo", "38200000", "Casa", 3088);
insert into endereco values ("rua Piracicaba", "18", "Bom Jardim", "34567830", "Apto 202", 3988);
insert into endereco values ("Alameda Vicente Pizon", "123", "Vila Olimpia", "04520322", "Apto 07", 9422);

insert into grupo values ("Gerente", "Gerentes da empresa");
insert into grupo values ("Funcionario", "Funcionario da empresa");
insert into grupo values ("Supervisor", "Supervisor da empresa");
insert into grupo values ("Diretor", "Diretor da empresa");

insert into permissao values ("Cadastro", "Cadastro da empresa");
insert into permissao values ("Financeiro", "Financeiro da empresa");
insert into permissao values ("Vendas", "Vendas da empresa");
insert into permissao values ("Estoque", "Estoque da empresa");
insert into permissao values ("Producao", "Producao da empresa");

insert into grupo_permissao values ("Gerente", "Cadastro");
insert into grupo_permissao values ("Gerente", "Vendas");
insert into grupo_permissao values ("Funcionario", "Cadastro");
insert into grupo_permissao values ("Diretor", "Cadastro");
insert into grupo_permissao values ("Diretor", "Financeiro");
insert into grupo_permissao values ("Diretor", "Vendas");