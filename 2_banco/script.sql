-- Limpeza automática caso o script seja executado mais de uma vez:

DROP TABLE IF EXISTS TB_ALERTA_EMERGENCIA;
DROP TABLE IF EXISTS TB_SENSOR_MONITORAMENTO;
DROP TABLE IF EXISTS TB_CIDADAO;
DROP TABLE IF EXISTS TB_REGIAO_RISCO;

-- 1. CRIAÇÃO DAS TABELAS (DDL)

CREATE TABLE TB_REGIAO_RISCO (
    id_regiao INTEGER NOT NULL,
    nm_bairro VARCHAR(100) NOT NULL,
    nm_cidade VARCHAR(100) NOT NULL,
    vl_nivel_critico_mm NUMERIC(6,2) NOT NULL,
    nr_populacao_estimada INTEGER NOT NULL,
    CONSTRAINT PK_REGIAO_RISCO PRIMARY KEY (id_regiao),
    CONSTRAINT CK_REGIAO_NIVEL_CRITICO CHECK (vl_nivel_critico_mm > 0),
    CONSTRAINT CK_REGIAO_POPULACAO CHECK (nr_populacao_estimada > 0)
);

CREATE TABLE TB_CIDADAO (
    id_cidadao INTEGER NOT NULL,
    nm_cidadao VARCHAR(120) NOT NULL,
    nr_cpf VARCHAR(14) NOT NULL,
    nr_telefone VARCHAR(20) NOT NULL,
    st_mobilidade_reduzida CHAR(1) NOT NULL,
    id_regiao INTEGER NOT NULL,
    CONSTRAINT PK_CIDADAO PRIMARY KEY (id_cidadao),
    CONSTRAINT UK_CIDADAO_CPF UNIQUE (nr_cpf),
    CONSTRAINT CK_CIDADAO_MOBILIDADE CHECK (st_mobilidade_reduzida IN ('S', 'N')),
    CONSTRAINT FK_CIDADAO_REGIAO FOREIGN KEY (id_regiao) 
        REFERENCES TB_REGIAO_RISCO (id_regiao)
);

CREATE TABLE TB_SENSOR_MONITORAMENTO (
    id_sensor INTEGER NOT NULL,
    cd_serie VARCHAR(30) NOT NULL,
    tp_sensor VARCHAR(50) NOT NULL,
    vl_leitura_atual_mm NUMERIC(6,2) NOT NULL,
    st_operacional VARCHAR(20) NOT NULL,
    id_regiao INTEGER NOT NULL,
    CONSTRAINT PK_SENSOR PRIMARY KEY (id_sensor),
    CONSTRAINT UK_SENSOR_SERIE UNIQUE (cd_serie),
    CONSTRAINT CK_SENSOR_LEITURA CHECK (vl_leitura_atual_mm >= 0),
    CONSTRAINT CK_SENSOR_STATUS CHECK (st_operacional IN ('ATIVO', 'MANUTENCAO', 'INATIVO')),
    CONSTRAINT FK_SENSOR_REGIAO FOREIGN KEY (id_regiao) 
        REFERENCES TB_REGIAO_RISCO (id_regiao)
);

CREATE TABLE TB_ALERTA_EMERGENCIA (
    id_alerta INTEGER NOT NULL,
    nv_gravidade VARCHAR(20) NOT NULL,
    ds_mensagem VARCHAR(255) NOT NULL,
    dt_hora_emissao VARCHAR(25) NOT NULL,
    id_regiao INTEGER NOT NULL,
    id_sensor INTEGER NOT NULL,
    CONSTRAINT PK_ALERTA PRIMARY KEY (id_alerta),
    CONSTRAINT CK_ALERTA_GRAVIDADE CHECK (nv_gravidade IN ('ATENCAO', 'ALERTA', 'EMERGENCIA')),
    CONSTRAINT FK_ALERTA_REGIAO FOREIGN KEY (id_regiao) 
        REFERENCES TB_REGIAO_RISCO (id_regiao),
    CONSTRAINT FK_ALERTA_SENSOR FOREIGN KEY (id_sensor) 
        REFERENCES TB_SENSOR_MONITORAMENTO (id_sensor)
);

-- 2. INSERÇÃO DE DADOS DE EXEMPLO (DML)

INSERT INTO TB_REGIAO_RISCO (id_regiao, nm_bairro, nm_cidade, vl_nivel_critico_mm, nr_populacao_estimada)
VALUES (1, 'Jardim Pantanal', 'São Paulo', 60.00, 45000);

INSERT INTO TB_REGIAO_RISCO (id_regiao, nm_bairro, nm_cidade, vl_nivel_critico_mm, nr_populacao_estimada)
VALUES (2, 'Vila Aimoré', 'São Paulo', 55.00, 28000);

INSERT INTO TB_REGIAO_RISCO (id_regiao, nm_bairro, nm_cidade, vl_nivel_critico_mm, nr_populacao_estimada)
VALUES (3, 'Parque Novo Mundo', 'São Paulo', 70.00, 32000);

INSERT INTO TB_CIDADAO (id_cidadao, nm_cidadao, nr_cpf, nr_telefone, st_mobilidade_reduzida, id_regiao)
VALUES (1, 'Carlos Eduardo Silva', '123.456.789-01', '(11) 98765-4321', 'S', 1);

INSERT INTO TB_CIDADAO (id_cidadao, nm_cidadao, nr_cpf, nr_telefone, st_mobilidade_reduzida, id_regiao)
VALUES (2, 'Maria Fernanda Oliveira', '234.567.890-12', '(11) 97654-3210', 'N', 1);

INSERT INTO TB_CIDADAO (id_cidadao, nm_cidadao, nr_cpf, nr_telefone, st_mobilidade_reduzida, id_regiao)
VALUES (3, 'Roberto Almeida Santos', '345.678.901-23', '(11) 96543-2109', 'S', 2);

INSERT INTO TB_SENSOR_MONITORAMENTO (id_sensor, cd_serie, tp_sensor, vl_leitura_atual_mm, st_operacional, id_regiao)
VALUES (1, 'SN-PLUV-001', 'Pluviometro Digital', 68.50, 'ATIVO', 1);

INSERT INTO TB_SENSOR_MONITORAMENTO (id_sensor, cd_serie, tp_sensor, vl_leitura_atual_mm, st_operacional, id_regiao)
VALUES (2, 'SN-NIV-002', 'Sensor Ultrassonico de Nivel', 52.00, 'ATIVO', 2);

INSERT INTO TB_SENSOR_MONITORAMENTO (id_sensor, cd_serie, tp_sensor, vl_leitura_atual_mm, st_operacional, id_regiao)
VALUES (3, 'SN-PLUV-003', 'Pluviometro Digital', 15.20, 'ATIVO', 3);

INSERT INTO TB_ALERTA_EMERGENCIA (id_alerta, nv_gravidade, ds_mensagem, dt_hora_emissao, id_regiao, id_sensor)
VALUES (1, 'EMERGENCIA', 'Risco iminente de transbordamento. Inicie evacuacao preventiva para pontos altos.', '2026-11-16 14:30:00', 1, 1);

INSERT INTO TB_ALERTA_EMERGENCIA (id_alerta, nv_gravidade, ds_mensagem, dt_hora_emissao, id_regiao, id_sensor)
VALUES (2, 'ALERTA', 'Volume de chuva proximo ao limite critico da bacia. Prepare itens essenciais.', '2026-11-16 14:15:00', 2, 2);

-- 3. CONSULTAS QUE RESPONDEM PERGUNTAS DO NEGÓCIO 

-- Pergunta 1: Quais alertas foram emitidos, em qual bairro, qual sensor detectou e qual a leitura comparada ao limite crítico?
SELECT 
    a.id_alerta,
    a.nv_gravidade,
    a.dt_hora_emissao,
    r.nm_bairro,
    r.nm_cidade,
    s.cd_serie AS sensor_origem,
    s.vl_leitura_atual_mm,
    r.vl_nivel_critico_mm,
    a.ds_mensagem
FROM TB_ALERTA_EMERGENCIA a
INNER JOIN TB_REGIAO_RISCO r ON a.id_regiao = r.id_regiao
INNER JOIN TB_SENSOR_MONITORAMENTO s ON a.id_sensor = s.id_sensor
ORDER BY a.id_alerta DESC;

-- Pergunta 2: Quais moradores possuem mobilidade reduzida em áreas com alertas ativos para priorização da Defesa Civil?
SELECT 
    c.nm_cidadao,
    c.nr_telefone,
    r.nm_bairro,
    a.nv_gravidade,
    a.ds_mensagem
FROM TB_CIDADAO c
INNER JOIN TB_REGIAO_RISCO r ON c.id_regiao = r.id_regiao
INNER JOIN TB_ALERTA_EMERGENCIA a ON r.id_regiao = a.id_regiao
WHERE c.st_mobilidade_reduzida = 'S'
ORDER BY a.nv_gravidade DESC, c.nm_cidadao ASC;