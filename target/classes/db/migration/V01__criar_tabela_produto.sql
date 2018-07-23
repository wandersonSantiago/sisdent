CREATE TABLE Produto (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    codigo_barras VARCHAR(50) NOT NULL,
    nome VARCHAR(80) NOT NULL,
    descricao TEXT NOT NULL,
    valor DECIMAL(10, 2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE categoria (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO categoria VALUES (0, 'Limpeza');
INSERT INTO categoria VALUES (0, 'Higiene bucal');
INSERT INTO categoria VALUES (0, 'Creme dental');
INSERT INTO categoria VALUES (0, 'Aparelhos');
