# 💧 Simulador de Hidrômetro

Um simulador de hidrômetro analógico em Java, construído com foco em boas práticas de design e arquitetura de software, incluindo a aplicação de Padrões de Projeto como Observer, Strategy e Facade.

![Status](https://img.shields.io/badge/status-concluído-green.svg)
![Linguagem](https://img.shields.io/badge/linguagem-Java-blue.svg)
![UI](https://img.shields.io/badge/ui-Java%20Swing-orange.svg)
![Licença](https://img.shields.io/badge/licença-MIT-blue.svg)

---

## 📖 Sobre o Projeto

Este projeto simula o funcionamento de um hidrômetro (medidor de água) em tempo real. Ele apresenta uma interface gráfica que exibe a leitura de um medidor analógico, atualizando o consumo com base em perfis de uso configuráveis.

O principal objetivo não é apenas a simulação em si, mas servir como um estudo de caso prático de como estruturar uma aplicação Java de forma coesa, desacoplada e de fácil manutenção, utilizando princípios SOLID e Padrões de Projeto.

## ✨ Features

* **Simulação Visual:** Renderiza uma imagem realista de um hidrômetro analógico que reflete o consumo.
* **Configuração Externa:** Todo o comportamento da simulação (bitola, tempo de execução, pressão, etc.) é controlado por um arquivo `.properties`.
* **Perfis de Consumo:** Utiliza o Padrão de Projeto *Strategy* para simular diferentes padrões de consumo de água (madrugada, manhã, tarde e noite).
* **Controle em Tempo Real:** Permite que o usuário ajuste a vazão da água em tempo real através de um slider na interface.
* **Persistência de Medições:** Salva automaticamente um snapshot (`.jpeg`) do hidrômetro toda vez que o valor de m³ inteiro é incrementado.
* **Saída de Dados:** Exibe o log de medições no console a cada atualização.

## 🏗️ Arquitetura e Padrões de Projeto

A arquitetura do sistema foi desenhada para ser modular e extensível. Cada pacote possui uma responsabilidade única, e a comunicação entre eles é feita de forma desacoplada. Os seguintes padrões foram fundamentais na sua construção:

* **Observer:** Usado para desacoplar o motor da simulação (`HidrometroSimulator`) das "visualizações" (`Display`, `Saida`). O simulador notifica os observadores sobre as atualizações sem precisar conhecê-los diretamente.
* **Strategy:** Permite que o algoritmo de cálculo de consumo de água seja intercambiável. As classes `PerfilMadrugada`, `PerfilManha`, etc., são estratégias concretas que podem ser adicionadas ou removidas sem alterar o simulador.
* **Facade:** A classe `Display` atua como uma fachada para o complexo subsistema de UI. Ela simplifica a interação, escondendo a lógica de renderização (`HidrometroRenderer`), persistência de imagens (`ImagePersistenceService`) e componentes de controle (`ControleVaoPanel`).
* **Factory / Princípio da Responsabilidade Única (SRP):** A classe `ConfigLoader` tem a única responsabilidade de ler e fazer o "parse" do arquivo de configuração, funcionando como uma fábrica que produz objetos `SimulatorConfig`.
* **Composition Root:** A classe `Controller` centraliza a criação e a conexão de todos os objetos do sistema, garantindo que as dependências sejam injetadas corretamente e que os componentes permaneçam desacoplados entre si.

## 🛠️ Tecnologias Utilizadas

* **Java:** Linguagem principal do projeto.
* **Java Swing:** Para a construção da interface gráfica.
* **Java 2D Graphics API:** Para a renderização customizada do hidrômetro.
* **Java Concurrency (`ExecutorService`):** Para o salvamento de imagens em background, sem travar a UI.

## 🚀 Como Executar

**Pré-requisitos:**
* Java Development Kit (JDK) 11 ou superior instalado.

**Passos:**

1.  **Clone o repositório:**
    ```bash
    git clone https://[URL-DO-SEU-REPOSITORIO]/simulador-hidrometro.git
    cd simulador-hidrometro
    ```

2.  **Crie o arquivo de configuração:**
    Na raiz do projeto, crie um arquivo chamado `config.properties` com o seguinte conteúdo:
    ```properties
    # --- Configurações do Hidrômetro ---
    bitola=MEIA_POLEGADA
    matricula=12345678

    # --- Configurações da Simulação ---
    tempoExecucao=300
    intervaloAtualizacao=1000
    escalaDeTempo=60
    pressaoMinima=10.0
    pressaoMaxima=15.0
    simularAr=false

    # --- Configurações dos Perfis de Consumo (Velocidade em m/s) ---
    perfilDeConsumo.madrugada.min=0.1
    perfilDeConsumo.madrugada.max=0.3
    perfilDeConsumo.madrugada.inicio=0
    perfilDeConsumo.madrugada.fim=5

    perfilDeConsumo.manha.min=1.5
    perfilDeConsumo.manha.max=2.5
    perfilDeConsumo.manha.inicio=6
    perfilDeConsumo.manha.fim=9

    perfilDeConsumo.tarde.min=0.8
    perfilDeConsumo.tarde.max=1.5
    perfilDeConsumo.tarde.inicio=10
    perfilDeConsumo.tarde.fim=18

    perfilDeConsumo.noite.min=1.2
    perfilDeConsumo.noite.max=2.0
    perfilDeConsumo.noite.inicio=19
    perfilDeConsumo.noite.fim=23
    ```

3.  **Compile e Execute:**

    * **Via IDE (Recomendado):**
        * Abra o projeto na sua IDE preferida (IntelliJ, Eclipse, VS Code).
        * Execute a classe `Main.java`.

    * **Via Linha de Comando:**
        Navegue até o diretório `src` e execute:
        ```bash
        # Compile todos os arquivos .java (exemplo simplificado)
        javac main/java/br/com/simulador/Main.java
        
        # Execute a classe Main
        java main.java.br.com.simulador.Main
        ```

## 📁 Estrutura do Projeto

src/
└── main/java/br/com/simulador/
├── Main.java                   # Ponto de entrada da aplicação
├── controller/
│   └── Controller.java         # Orquestrador principal (Composition Root)
├── config/
│   ├── Bitola.java             # Enum para as bitolas
│   ├── ConfigLoader.java       # Factory para carregar configurações
│   └── SimulatorConfig.java    # DTO com os dados de configuração
├── hidrometro/
│   └── display/
│       ├── Display.java            # Facade para a UI (Observador)
│       ├── ControleVazaoPanel.java # Componente de UI do slider
│       ├── HidrometroRenderer.java # Especialista em desenhar o hidrômetro
│       └── ImagePersistenceService.java # Especialista em salvar imagens
│   ├── ControleVazao.java      # Objeto de estado para a vazão
│   ├── Entrada.java            # Calcula o fluxo de entrada no medidor
│   ├── HidrometroSimulator.java# O motor da simulação (Subject)
│   ├── Medidor.java            # Representa o estado do medidor
│   └── Saida.java              # Observador que loga no console (Record)
├── observer/
│   └── Observador.java         # Interface do Padrão Observer
├── strategy/
│   ├── PerfilDeConsumoStrategy.java # Interface do Padrão Strategy
│   ├── PerfilMadrugada.java    # Implementações concretas
│   ├── PerfilManha.java        # ...
│   ├── PerfilNoite.java
│   └── PerfilTarde.java

