# 💻 Simulador de Hidrômetro em Java

Este projeto é um simulador de hidrômetro desenvolvido em Java com a biblioteca Swing para a interface gráfica. Ele simula o consumo de água ao longo do dia, aplicando diferentes perfis de consumo e permitindo o controle de vazão em tempo real.

## ✨ Funcionalidades

* **Interface Gráfica**: Visualização do hidrômetro em tempo real com Swing.
* **Perfis de Consumo**: Simulação de consumo variável para madrugada, manhã, tarde e noite (Padrão Strategy).
* **Controle de Vazão**: Um slider permite ajustar a vazão de 0% a 100%.
* **Simulação de Ar**: Quando a vazão é 0%, o sistema pode simular a passagem de ar, registrando consumo indevido (configurável).
* **Exportação de Imagens**: Salva automaticamente uma imagem (JPEG) do medidor a cada metro cúbico completado.

## 📸 Screenshot

*(**DICA:** Tire um print da sua aplicação rodando e coloque aqui! Isso torna o repositório muito mais atraente. Você pode arrastar a imagem diretamente para a edição do README no GitHub.)*

![Screenshot do Simulador](caminho/para/sua/imagem.png)

## 🚀 Como Executar

1.  **Pré-requisitos**:
    * Java JDK (versão 11 ou superior) instalado.

2.  **Passos**:
    * Clone o repositório: `git clone https://github.com/Rodriguesmath/Simulador-Hidrometro.git`
    * Abra o projeto em sua IDE preferida (IntelliJ, Eclipse, etc.).
    * Execute a classe `Main.java` localizada em `src/main/java/br/com/simulador/`.

## ⚙️ Configuração

O comportamento da simulação pode ser ajustado através do arquivo `config/config.txt`.

* `matricula`: Sua matrícula, usada para nomear a pasta de imagens salvas.
* `simularAr`: `true` ou `false` para ativar a simulação de ar com vazão zero.
* `bitola`, `tempoExecucao`, etc.

---
