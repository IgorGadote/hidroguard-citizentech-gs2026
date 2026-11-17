# HidroGuard — CitizenTech | Global Solution 2026/2 (FIAP)

O **HidroGuard** é uma plataforma CitizenTech de monitoramento pluviométrico preventivo e emissão de alertas comunitários de enchentes. A solução conecta sensores instalados em bacias críticas a uma rede de avisos diretos aos moradores, mapeando previamente cidadãos com mobilidade reduzida, idosos acamados e pessoas com deficiência para acionar o resgate prioritário da Defesa Civil antes que o transbordamento ocorra.

---

## Identificação
* **Integrante:** Igor Gadote — **RM:** `567747`
* **Curso / Turma:** 2º Ano de Engenharia de Software — Turma `2ESOR` (FIAP)

## Links do Projeto
* **Vídeo Pitch (Até 3 minutos):** https://youtu.be/COLE_AQUI_O_LINK_DO_SEU_VIDEO
* **Protótipo Web Publicado (GitHub Pages):** https://igorgadote.github.io/hidroguard-citizentech-gs2026/
* **Repositório Oficial:** https://github.com/IgorGadote/hidroguard-citizentech-gs2026

---

## Critérios CitizenTech e Conexão com os ODS da ONU (Agenda 2030)

1. **Cidadão Beneficiado:** Moradores de regiões de várzea e encostas sujeitas a inundações severas (como Jardim Pantanal, Vila Aimoré e Parque Novo Mundo, em São Paulo), com foco especial na proteção de pessoas com mobilidade reduzida.
2. **Problema Resolvido:** Falta de alertas hiperlocais antecipados e ausência de mapeamento prévio de moradores vulneráveis que não conseguem evacuar suas residências sozinhos durante chuvas extremas.
3. **Ecossistema e Medição de Impacto:** Integra moradores, lideranças comunitárias e agentes da Defesa Civil. O impacto é medido pela antecedência dos avisos emitidos, pelo total de famílias vulneráveis cadastradas na rede e pela redução do tempo de resgate prioritário.

### Objetivos de Desenvolvimento Sustentável (ODS) Atendidos:
* **ODS 11 — Cidades e Comunidades Sustentáveis:** Contribui diretamente para a meta 11.5 ao reduzir o número de pessoas afetadas e os impactos humanos e econômicos causados por desastres hidrometeorológicos em comunidades vulneráveis.
* **ODS 13 — Ação Contra a Mudança Global do Clima:** Contribui para a meta 13.1 ao fortalecer a resiliência e a capacidade de adaptação comunitária frente a eventos climáticos extremos por meio de monitoramento preventivo e protocolos de evacuação.

---

## Arquitetura do Projeto: Um Domínio, Três Entregas

O mesmo domínio de negócio foi modelado utilizando 4 entidades centrais (**Região de Risco**, **Cidadão**, **Sensor de Monitoramento** e **Alerta de Emergência**) nas três partes exigidas, aplicando exclusivamente os conteúdos previstos até a Fase 4 (sem frameworks ou bibliotecas externas):

### 1. Sistema Java POO (`/1_java` e `/4_diagramas/Diagrama_Classes_UML.png`)
* **Modelagem e Camadas:** Arquitetura dividida em camadas independentes (`model`, `service` e `main`). As 4 classes de domínio possuem atributos privados, construtores, getters/setters, `toString()` customizado e associações entre objetos (`Cidadao`, `SensorMonitoramento` e `AlertaEmergencia` associados a `RegiaoRisco`).
* **CRUD Completo e Desafio Extra:** Menu interativo no console mantendo os dados em listas (`ArrayList`), permitindo Cadastrar, Listar, Buscar por ID, Atualizar e Remover registros (com validação de integridade caso a região possua cidadãos ou sensores vinculados).
* **Regras de Negócio e Validações:** Tratamento de entradas inválidas no console (letras em campos numéricos, textos vazios, valores negativos ou CPFs duplicados) sem interromper a execução, além de disparo automático de `AlertaEmergencia` quando a leitura de um sensor atinge ou supera o limite crítico da região.
* **Como executar no VS Code / Terminal:**
  * Pela extensão Java do VS Code: abra o arquivo `Main.java` dentro de `1_java` e clique em **Run**.
  * Ou via terminal (a partir da pasta `1_java`):
    * Compilar: `javac -encoding UTF-8 -d bin src/model/*.java src/service/*.java src/main/*.java`
    * Executar: `java -cp bin main.Main`
    * *(Caso utilize a estrutura completa de diretórios `br/com/fiap/hidroguard`, execute: `javac -encoding UTF-8 -d bin src/br/com/fiap/hidroguard/model/*.java src/br/com/fiap/hidroguard/service/*.java src/br/com/fiap/hidroguard/main/*.java` e `java -cp bin br.com.fiap.hidroguard.main.Main`).*

### 2. Banco de Dados Relacional (`/2_banco/script.sql` e `/4_diagramas/Diagrama_ER_Banco.png`)
* **Diagrama Entidade-Relacionamento (DER):** Representação relacional das 4 tabelas (`TB_REGIAO_RISCO`, `TB_CIDADAO`, `TB_SENSOR_MONITORAMENTO` e `TB_ALERTA_EMERGENCIA`), destacando chaves primárias (`PK`), chaves estrangeiras (`FK`), constraints `UNIQUE` (`UK`) e cardinalidades (`1:N`).
* **Script SQL (`script.sql`):**
  * **DDL:** Comandos `DROP TABLE IF EXISTS` e `CREATE TABLE` com `PRIMARY KEY`, `FOREIGN KEY`, `NOT NULL`, `UNIQUE` e `CHECK`.
  * **DML:** Comandos `INSERT INTO` populando todas as tabelas com dados de exemplo.
  * **DQL (Diferencial):** Consultas `SELECT` com `INNER JOIN` e `ORDER BY` que respondem a perguntas do domínio (comparativo entre leitura do sensor e limite crítico do bairro, e listagem prioritária de moradores com mobilidade reduzida em áreas com alertas ativos).

### 3. Protótipo Web (Raiz do repositório — `index.html`, `monitoramento.html`, `cadastro.html`, `style.css` e `script.js`)
* **Estrutura e Semântica:** Composto por 3 páginas conectadas por um menu de navegação idêntico e responsivo, escritas com HTML5 semântico, CSS3 puro com alto contraste e JavaScript Vanilla (`script.js`).
* **Páginas e Funcionalidades no DOM:**
  * `index.html` (**Início e Regiões**): Apresentação da CitizenTech, painel de indicadores dinâmicos, busca instantânea de regiões por bairro via DOM e conexão com os ODS 11 e 13.
  * `monitoramento.html` (**Monitoramento e Alertas**): Painel interativo de alertas de emergência com filtros combinados em tempo real pelo DOM (por região de risco, nível de gravidade e palavra-chave) e telemetria dos sensores.
  * `cadastro.html` (**Cadastro Cidadão**): Formulário de inscrição de moradores com validação em JavaScript exibindo mensagens na própria página, persistência via `localStorage` e filtro dinâmico de resgate prioritário.

---

## Estrutura de Arquivos do Projeto

* `GS_2026_2_HidroGuard/`
  * `1_java/` -> `src/` (Camadas `model`, `service` e `main` do sistema Java POO)
  * `2_banco/` -> `script.sql` (DDL, DML e consultas `SELECT` com `INNER JOIN`)
  * `4_diagramas/` -> `Diagrama_Classes_UML.png` e `Diagrama_ER_Banco.png`
  * `index.html`, `monitoramento.html`, `cadastro.html`, `style.css` e `script.js` (Protótipo Web)
  * `integrantes_e_pitch.txt` (Identificação e links de entrega)
  * `README.md` (Documentação geral do projeto)

---

## Declaração de Uso de IA Generativa

Em conformidade com as regras da Global Solution 2026/2, declaro que a ferramenta Google Gemini foi utilizada pontualmente como apoio para pesquisa teórica sobre as metas dos ODS da ONU e revisão textual desta documentação. O desenvolvimento do código, a modelagem de dados e os testes de execução do projeto foram realizados, compreendidos e validados pelo autor Igor Gadote.