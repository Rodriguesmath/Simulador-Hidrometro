# 💻 Simulador de Hidrômetro em Java

Este projeto é um simulador de hidrômetro desenvolvido em Java com a biblioteca Swing para a interface gráfica. Ele simula o consumo de água ao longo do dia, aplicando diferentes perfis de consumo e permitindo o controle de vazão em tempo real.

## ✨ Funcionalidades

* **Interface Gráfica**: Visualização do hidrômetro em tempo real com Swing.
* **Perfis de Consumo**: Simulação de consumo variável para madrugada, manhã, tarde e noite (Padrão Strategy).
* **Controle de Vazão**: Um slider permite ajustar a vazão de 0% a 100%.
* **Simulação de Ar**: Quando a vazão é 0%, o sistema pode simular a passagem de ar, registrando consumo indevido (configurável).
* **Exportação de Imagens**: Salva automaticamente uma imagem (JPEG) do medidor a cada metro cúbico completado.

## 📸 Screenshot
![Screenshot do Simulador](caminho/para/sua/imagem.png)

## 🚀 Como Executar

1.  **Pré-requisitos**:
    * Java JDK (versão 11 ou superior) instalado.

2.  **Passos**:
    * Clone o repositório: `git clone https://github.com/Rodriguesmath/Simulador-Hidrometro.git`
    * Abra o projeto em sua IDE preferida (IntelliJ, Eclipse, etc.).
    * Execute a classe `Main.java` localizada em `src/main/java/br/com/simulador/`.

## ⚙️ Configuração

O comportamento da simulação pode ser ajustado através do arquivo `config/config.txt`. Abaixo estão os parâmetros disponíveis:

### Configurações Gerais da Simulação
* `bitola`: Define o diâmetro do hidrômetro em polegadas. As opções válidas são `1/2`, `3/4`, `1`, `1 1/2`, `2`, `3`, `4`.
* `tempoExecucao`: Determina a duração total da simulação em segundos. O valor `-1` pode ser usado para uma execução infinita.
* `intervaloAtualizacao`: Controla a pausa, em milissegundos, no mundo real entre cada atualização visual.
* `escalaDeTempo`: Define quantos segundos o tempo da simulação avança a cada atualização.

### Parâmetros Físicos
* `pressaoMinima`: A pressão mínima da água na rede, medida em bar.
* `pressaoMaxima`: A pressão máxima da água na rede, medida em bar.

### Perfis de Consumo
O sistema utiliza perfis para simular o consumo em diferentes períodos do dia (Madrugada, Manhã, Tarde e Noite). Para cada perfil, é possível definir:
* A hora de início e fim do período (ex: `madrugada_inicio`, `madrugada_fim`).
* A faixa de velocidade do fluxo de água em m/s (ex: `madrugada_vel_min`, `madrugada_vel_max`).

### Funcionalidades Adicionais
* `simularAr`: Um valor booleano (`true` ou `false`) que ativa ou desativa a simulação da passagem de ar quando a vazão é 0%.
* `matricula`: Define a Matrícula SUAP que será usada para nomear o diretório onde as medições são salvas.
