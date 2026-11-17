// 1. DADOS DO DOMÍNIO

const regioesRisco = [
    {
        idRegiao: 1,
        nomeBairro: "Jardim Pantanal",
        cidade: "São Paulo",
        nivelCriticoChuvaMm: 60.0,
        populacaoEstimada: 45000
    },
    {
        idRegiao: 2,
        nomeBairro: "Vila Aimoré",
        cidade: "São Paulo",
        nivelCriticoChuvaMm: 55.0,
        populacaoEstimada: 28000
    },
    {
        idRegiao: 3,
        nomeBairro: "Parque Novo Mundo",
        cidade: "São Paulo",
        nivelCriticoChuvaMm: 70.0,
        populacaoEstimada: 32000
    }
];

const sensoresMonitoramento = [
    {
        idSensor: 1,
        codigoSerie: "SN-PLUV-001",
        tipoSensor: "Pluviômetro Digital",
        leituraAtualMm: 68.5,
        statusOperacional: "ATIVO",
        idRegiao: 1
    },
    {
        idSensor: 2,
        codigoSerie: "SN-NIV-002",
        tipoSensor: "Sensor Ultrassônico de Nível",
        leituraAtualMm: 52.0,
        statusOperacional: "ATIVO",
        idRegiao: 2
    },
    {
        idSensor: 3,
        codigoSerie: "SN-PLUV-003",
        tipoSensor: "Pluviômetro Digital",
        leituraAtualMm: 15.2,
        statusOperacional: "ATIVO",
        idRegiao: 3
    }
];

const alertasEmergencia = [
    {
        idAlerta: 1,
        nivelGravidade: "EMERGENCIA",
        mensagemOrientacao: "Risco iminente de transbordamento. Inicie evacuação preventiva para pontos altos.",
        dataHoraEmissao: "2026-11-16 14:30:00",
        idRegiao: 1,
        idSensor: 1
    },
    {
        idAlerta: 2,
        nivelGravidade: "ALERTA",
        mensagemOrientacao: "Volume de chuva próximo ao limite crítico da bacia. Prepare itens essenciais.",
        dataHoraEmissao: "2026-11-16 14:15:00",
        idRegiao: 2,
        idSensor: 2
    },
    {
        idAlerta: 3,
        nivelGravidade: "ATENCAO",
        mensagemOrientacao: "Chuva moderada contínua detectada na cabeceira. Monitoramento preventivo em andamento.",
        dataHoraEmissao: "2026-11-16 13:40:00",
        idRegiao: 3,
        idSensor: 3
    }
];

const cidadaosPadrao = [
    {
        idCidadao: 1,
        nome: "Carlos Eduardo Silva",
        cpf: "123.456.789-01",
        telefone: "(11) 98765-4321",
        possuiMobilidadeReduzida: true,
        idRegiao: 1
    },
    {
        idCidadao: 2,
        nome: "Maria Fernanda Oliveira",
        cpf: "234.567.890-12",
        telefone: "(11) 97654-3210",
        possuiMobilidadeReduzida: false,
        idRegiao: 1
    },
    {
        idCidadao: 3,
        nome: "Roberto Almeida Santos",
        cpf: "345.678.901-23",
        telefone: "(11) 96543-2109",
        possuiMobilidadeReduzida: true,
        idRegiao: 2
    }
];

// Carrega cidadãos do localStorage se houver, ou usa os dados do início
let cidadaos = carregarCidadaos();

function carregarCidadaos() {
    try {
        const salvos = localStorage.getItem("hidroguard_cidadaos");
        if (salvos) {
            return JSON.parse(salvos);
        }
    } catch (e) {
        // Caso o navegador bloqueie localStorage em arquivo local, mantém em memória
    }
    return [...cidadaosPadrao];
}

function salvarCidadaos() {
    try {
        localStorage.setItem("hidroguard_cidadaos", JSON.stringify(cidadaos));
    } catch (e) {
        // Ignora o erro se executado restrito sem storage
    }
}

// Funções auxiliares
function obterRegiaoPorId(idRegiao) {
    return regioesRisco.find(r => r.idRegiao === Number(idRegiao)) || null;
}

function obterSensorPorId(idSensor) {
    return sensoresMonitoramento.find(s => s.idSensor === Number(idSensor)) || null;
}

// MENU MOBILE (TODAS AS PÁGINAS)

function inicializarMenuMobile() {
    const btnMenu = document.getElementById("btn-menu-mobile");
    const navMenu = document.getElementById("nav-menu");

    if (btnMenu && navMenu) {
        btnMenu.addEventListener("click", () => {
            const aberto = navMenu.classList.toggle("aberto");
            btnMenu.setAttribute("aria-expanded", aberto ? "true" : "false");
        });
    }
}

// PÁGINA 1: INDEX.HTML

function inicializarPaginaInicial() {
    const containerMetricas = document.getElementById("resumo-metricas-home");
    const containerRegioes = document.getElementById("lista-regioes-home");
    const inputBuscaRegiao = document.getElementById("busca-regiao-home");

    if (containerMetricas) {
        const totalPrioritarios = cidadaos.filter(c => c.possuiMobilidadeReduzida).length;
        const alertasCriticos = alertasEmergencia.filter(a => a.nivelGravidade === "EMERGENCIA" || a.nivelGravidade === "ALERTA").length;

        containerMetricas.innerHTML = `
            <div class="metrica-item">
                <span class="metrica-valor">${regioesRisco.length}</span>
                <span class="metrica-rotulo">Regiões Monitoradas</span>
            </div>
            <div class="metrica-item">
                <span class="metrica-valor">${sensoresMonitoramento.length}</span>
                <span class="metrica-rotulo">Sensores Ativos</span>
            </div>
            <div class="metrica-item">
                <span class="metrica-valor">${alertasCriticos}</span>
                <span class="metrica-rotulo">Alertas Críticos</span>
            </div>
            <div class="metrica-item">
                <span class="metrica-valor">${totalPrioritarios}</span>
                <span class="metrica-rotulo">Moradores Prioritários</span>
            </div>
        `;
    }

    function renderizarRegioesHome(termoFiltro = "") {
        if (!containerRegioes) return;

        const termo = termoFiltro.trim().toLowerCase();
        const filtradas = regioesRisco.filter(r =>
            r.nomeBairro.toLowerCase().includes(termo) ||
            r.cidade.toLowerCase().includes(termo)
        );

        if (filtradas.length === 0) {
            containerRegioes.innerHTML = `<p>Nenhuma região encontrada para "${termoFiltro}".</p>`;
            return;
        }

        containerRegioes.innerHTML = filtradas.map(regiao => {
            const sensorRegiao = sensoresMonitoramento.find(s => s.idRegiao === regiao.idRegiao);
            const leitura = sensorRegiao ? sensorRegiao.leituraAtualMm : 0;
            const ultrapassou = leitura >= regiao.nivelCriticoChuvaMm;

            return `
                <article class="card">
                    <span class="badge-gravidade ${ultrapassou ? 'badge-EMERGENCIA' : 'badge-ATENCAO'}">
                        ${ultrapassou ? 'Acima do Limite Crítico' : 'Monitoramento Ativo'}
                    </span>
                    <h3 style="margin-top: 12px;">${regiao.nomeBairro} (${regiao.cidade})</h3>
                    <p><strong>Limite Crítico da Bacia:</strong> ${regiao.nivelCriticoChuvaMm.toFixed(1)} mm</p>
                    <p><strong>Leitura Atual:</strong> ${leitura.toFixed(1)} mm</p>
                    <p><strong>População Estimada:</strong> ${regiao.populacaoEstimada.toLocaleString('pt-BR')} habitantes</p>
                </article>
            `;
        }).join("");
    }

    if (containerRegioes) {
        renderizarRegioesHome();
    }

    if (inputBuscaRegiao) {
        inputBuscaRegiao.addEventListener("input", (evento) => {
            renderizarRegioesHome(evento.target.value);
        });
    }
}

// PÁGINA 2: MONITORAMENTO.HTML

function inicializarPaginaMonitoramento() {
    const selectRegiao = document.getElementById("filtro-regiao");
    const selectGravidade = document.getElementById("filtro-gravidade");
    const inputBusca = document.getElementById("filtro-busca-alerta");
    const btnLimpar = document.getElementById("btn-limpar-filtros");
    const listaAlertasDom = document.getElementById("lista-alertas-dom");
    const contadorAlertas = document.getElementById("contador-alertas");
    const listaSensoresDom = document.getElementById("lista-sensores-dom");

    if (selectRegiao) {
        regioesRisco.forEach(regiao => {
            const option = document.createElement("option");
            option.value = String(regiao.idRegiao);
            option.textContent = `${regiao.nomeBairro} (${regiao.cidade})`;
            selectRegiao.appendChild(option);
        });
    }

    function aplicarFiltrosAlertas() {
        if (!listaAlertasDom) return;

        const regiaoSelecionada = selectRegiao ? selectRegiao.value : "TODAS";
        const gravidadeSelecionada = selectGravidade ? selectGravidade.value : "TODOS";
        const textoBusca = inputBusca ? inputBusca.value.trim().toLowerCase() : "";

        const alertasFiltrados = alertasEmergencia.filter(alerta => {
            const regiao = obterRegiaoPorId(alerta.idRegiao);
            const sensor = obterSensorPorId(alerta.idSensor);

            const bateRegiao = (regiaoSelecionada === "TODAS") || (String(alerta.idRegiao) === regiaoSelecionada);
            const bateGravidade = (gravidadeSelecionada === "TODOS") || (alerta.nivelGravidade === gravidadeSelecionada);

            const textoAlvo = `
                ${alerta.mensagemOrientacao}
                ${regiao ? regiao.nomeBairro : ""}
                ${sensor ? sensor.codigoSerie : ""}
            `.toLowerCase();

            const bateTexto = textoBusca === "" || textoAlvo.includes(textoBusca);

            return bateRegiao && bateGravidade && bateTexto;
        });

        if (contadorAlertas) {
            contadorAlertas.textContent = `Alertas Encontrados (${alertasFiltrados.length})`;
        }

        if (alertasFiltrados.length === 0) {
            listaAlertasDom.innerHTML = `
                <div class="card">
                    <p>Nenhum alerta corresponde aos filtros selecionados no momento.</p>
                </div>
            `;
            return;
        }

        listaAlertasDom.innerHTML = alertasFiltrados.map(alerta => {
            const regiao = obterRegiaoPorId(alerta.idRegiao);
            const sensor = obterSensorPorId(alerta.idSensor);

            return `
                <article class="alerta-item gravidade-${alerta.nivelGravidade}">
                    <div class="alerta-cabecalho">
                        <span class="badge-gravidade badge-${alerta.nivelGravidade}">${alerta.nivelGravidade}</span>
                        <span><strong>Data/Hora:</strong> ${alerta.dataHoraEmissao}</span>
                    </div>
                    <h3>Bairro: ${regiao ? regiao.nomeBairro : "Não identificado"} (${regiao ? regiao.cidade : ""})</h3>
                    <p>${alerta.mensagemOrientacao}</p>
                    <p style="font-size: 0.9rem; color: var(--cor-texto-suave);">
                        <strong>Sensor de Origem:</strong> ${sensor ? sensor.codigoSerie : "N/A"} (${sensor ? sensor.tipoSensor : ""})
                        &bull; <strong>Leitura Registrada:</strong> ${sensor ? sensor.leituraAtualMm.toFixed(1) + " mm" : "N/A"}
                    </p>
                </article>
            `;
        }).join("");
    }

    function renderizarSensores() {
        if (!listaSensoresDom) return;

        listaSensoresDom.innerHTML = sensoresMonitoramento.map(sensor => {
            const regiao = obterRegiaoPorId(sensor.idRegiao);
            const limite = regiao ? regiao.nivelCriticoChuvaMm : 100;
            const percentual = Math.min(100, Math.round((sensor.leituraAtualMm / limite) * 100));
            const corBarra = percentual >= 100 ? "var(--cor-emergencia)" : (percentual >= 80 ? "var(--cor-alerta)" : "var(--cor-primaria)");

            return `
                <article class="card">
                    <div class="alerta-cabecalho">
                        <strong>${sensor.codigoSerie}</strong>
                        <span class="badge-gravidade badge-ATENCAO">${sensor.statusOperacional}</span>
                    </div>
                    <h3 style="margin-top: 10px;">${sensor.tipoSensor}</h3>
                    <p><strong>Localização:</strong> ${regiao ? regiao.nomeBairro : "Desconhecida"}</p>
                    <p><strong>Leitura Atual:</strong> ${sensor.leituraAtualMm.toFixed(1)} mm / Limite: ${limite.toFixed(1)} mm (${percentual}%)</p>
                    <div class="barra-progresso-fundo" aria-label="Nível de chuva em relação ao limite crítico">
                        <div class="barra-progresso-preenchimento" style="width: ${percentual}%; background-color: ${corBarra};"></div>
                    </div>
                </article>
            `;
        }).join("");
    }

    if (selectRegiao) selectRegiao.addEventListener("change", aplicarFiltrosAlertas);
    if (selectGravidade) selectGravidade.addEventListener("change", aplicarFiltrosAlertas);
    if (inputBusca) inputBusca.addEventListener("input", aplicarFiltrosAlertas);

    if (btnLimpar) {
        btnLimpar.addEventListener("click", () => {
            if (selectRegiao) selectRegiao.value = "TODAS";
            if (selectGravidade) selectGravidade.value = "TODOS";
            if (inputBusca) inputBusca.value = "";
            aplicarFiltrosAlertas();
        });
    }

    aplicarFiltrosAlertas();
    renderizarSensores();
}

// PÁGINA 3: CADASTRO.HTML

function inicializarPaginaCadastro() {
    const formCidadao = document.getElementById("form-cidadao");
    const selectRegiaoCadastro = document.getElementById("regiao-cidadao");
    const listaCidadaosDom = document.getElementById("lista-cidadaos-dom");
    const checkboxFiltroPrioritarios = document.getElementById("filtro-prioritarios");
    const caixaFeedback = document.getElementById("mensagem-geral-form");

    if (selectRegiaoCadastro) {
        regioesRisco.forEach(regiao => {
            const option = document.createElement("option");
            option.value = String(regiao.idRegiao);
            option.textContent = `${regiao.nomeBairro} - Limite Crítico: ${regiao.nivelCriticoChuvaMm} mm`;
            selectRegiaoCadastro.appendChild(option);
        });
    }

    function renderizarCidadaos() {
        if (!listaCidadaosDom) return;

        const apenasPrioritarios = checkboxFiltroPrioritarios ? checkboxFiltroPrioritarios.checked : false;
        const filtrados = cidadaos.filter(c => !apenasPrioritarios || c.possuiMobilidadeReduzida);

        if (filtrados.length === 0) {
            listaCidadaosDom.innerHTML = `<p>Nenhum cidadão encontrado para este critério.</p>`;
            return;
        }

        listaCidadaosDom.innerHTML = filtrados.map(cidadao => {
            const regiao = obterRegiaoPorId(cidadao.idRegiao);
            return `
                <article class="cidadao-card">
                    <div class="alerta-cabecalho">
                        <strong>${cidadao.nome}</strong>
                        ${cidadao.possuiMobilidadeReduzida
                            ? '<span class="badge-prioritario">Resgate Prioritário (Mobilidade Reduzida)</span>'
                            : '<span class="badge-gravidade badge-ATENCAO">Monitoramento Padrão</span>'}
                    </div>
                    <p style="margin-top: 6px; font-size: 0.92rem;">
                        <strong>CPF:</strong> ${cidadao.cpf} &bull; <strong>Tel:</strong> ${cidadao.telefone}
                    </p>
                    <p style="font-size: 0.92rem; color: var(--cor-texto-suave);">
                        <strong>Região de Risco:</strong> ${regiao ? regiao.nomeBairro : "Não informada"}
                    </p>
                </article>
            `;
        }).join("");
    }

    function limparErrosFormulario() {
        const spansErro = ["erro-nome", "erro-cpf", "erro-telefone", "erro-regiao"];
        spansErro.forEach(id => {
            const el = document.getElementById(id);
            if (el) el.textContent = "";
        });

        const inputs = ["nome-cidadao", "cpf-cidadao", "telefone-cidadao", "regiao-cidadao"];
        inputs.forEach(id => {
            const el = document.getElementById(id);
            if (el) el.classList.remove("input-invalido");
        });
    }

    function exibirErroCampo(idInput, idSpanErro, mensagem) {
        const inputEl = document.getElementById(idInput);
        const spanEl = document.getElementById(idSpanErro);
        if (inputEl) inputEl.classList.add("input-invalido");
        if (spanEl) spanEl.textContent = mensagem;
    }

    function exibirFeedbackGeral(mensagem, tipo) {
        if (!caixaFeedback) return;
        caixaFeedback.textContent = mensagem;
        caixaFeedback.className = `caixa-feedback ${tipo}`;
    }

    if (formCidadao) {
        formCidadao.addEventListener("submit", (evento) => {
            evento.preventDefault();
            limparErrosFormulario();

            const nome = document.getElementById("nome-cidadao").value.trim();
            const cpf = document.getElementById("cpf-cidadao").value.trim();
            const telefone = document.getElementById("telefone-cidadao").value.trim();
            const idRegiao = document.getElementById("regiao-cidadao").value;
            const mobilidade = document.getElementById("mobilidade-cidadao").checked;

            let formularioValido = true;

            if (nome.length < 3) {
                exibirErroCampo("nome-cidadao", "erro-nome", "Informe o nome completo (mínimo de 3 caracteres).");
                formularioValido = false;
            }

            const apenasNumerosCpf = cpf.replace(/\D/g, "");
            if (apenasNumerosCpf.length !== 11) {
                exibirErroCampo("cpf-cidadao", "erro-cpf", "CPF inválido. Informe exatamente os 11 dígitos numéricos.");
                formularioValido = false;
            } else if (cidadaos.some(c => c.cpf.replace(/\D/g, "") === apenasNumerosCpf)) {
                exibirErroCampo("cpf-cidadao", "erro-cpf", "Este CPF já está cadastrado no sistema HidroGuard.");
                formularioValido = false;
            }

            const apenasNumerosTel = telefone.replace(/\D/g, "");
            if (apenasNumerosTel.length < 10) {
                exibirErroCampo("telefone-cidadao", "erro-telefone", "Informe um telefone válido com DDD (mínimo 10 dígitos).");
                formularioValido = false;
            }

            if (!idRegiao) {
                exibirErroCampo("regiao-cidadao", "erro-regiao", "Selecione a região de risco onde o cidadão reside.");
                formularioValido = false;
            }

            if (!formularioValido) {
                exibirFeedbackGeral("Por favor, corrija os campos destacados em vermelho antes de enviar.", "erro");
                return;
            }

            const novoId = cidadaos.length > 0 ? Math.max(...cidadaos.map(c => c.idCidadao)) + 1 : 1;
            const novoCidadao = {
                idCidadao: novoId,
                nome: nome,
                cpf: cpf,
                telefone: telefone,
                possuiMobilidadeReduzida: mobilidade,
                idRegiao: Number(idRegiao)
            };

            cidadaos.unshift(novoCidadao);
            salvarCidadaos();
            renderizarCidadaos();
            formCidadao.reset();

            exibirFeedbackGeral(`Cidadão "${nome}" cadastrado com sucesso na rede de alertas!`, "sucesso");
        });
    }

    if (checkboxFiltroPrioritarios) {
        checkboxFiltroPrioritarios.addEventListener("change", renderizarCidadaos);
    }

    renderizarCidadaos();
}

// INICIALIZAÇÃO AUTOMÁTICA

document.addEventListener("DOMContentLoaded", () => {
    inicializarMenuMobile();
    inicializarPaginaInicial();
    inicializarPaginaMonitoramento();
    inicializarPaginaCadastro();
});