# 💧 Simulador de Hidrômetro

Um simulador de hidrômetro analógico em Java, construído com foco em boas práticas de design e arquitetura de software, incluindo a aplicação de Padrões de Projeto como Observer, Strategy e Facade.

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow.svg)
![Linguagem](https://img.shields.io/badge/linguagem-Java-blue.svg)
![UI](https://img.shields.io/badge/ui-Java%20Swing-orange.svg)
![Licença](https://img.shields.io/badge/licença-MIT-blue.svg)
![UML: Mermaid](https://img.shields.io/badge/UML-Mermaid-blue.svg)

---

## 📝 Sumário

* [Como a UML foi Construída](#-como-a-uml-foi-construída)
* [Sobre o Projeto](#-sobre-o-projeto)
* [Features](#-features)
* [Arquitetura e Padrões de Projeto](#️-arquitetura-e-padrões-de-projeto)
* [Tecnologias Utilizadas](#️-tecnologias-utilizadas)
* [Como Executar](#-como-executar)
* [Estrutura do Projeto](#-estrutura-do-projeto)
* 
---
## 📐 Como a UML foi Construída

A modelagem UML deste projeto foi realizada através de um processo iterativo e colaborativo, utilizando o código-fonte como a "verdade absoluta" para a engenharia reversa da arquitetura. O processo seguiu os seguintes passos:

1.  **Análise do Código-Fonte:** Todas as classes Java foram analisadas para identificar suas responsabilidades, atributos e métodos públicos.
2.  **Identificação de Relacionamentos:** As conexões entre as classes (herança, composição, associação, dependência) foram mapeadas para entender o fluxo de dados e controle.
3.  **Visualização de Padrões:** A análise focou em como a estrutura do código refletia os Padrões de Projeto implementados, como *Observer*, *Strategy* e *Facade*.
4.  **Modelagem com Diagrama de Classes:** Um Diagrama de Classes completo foi criado para representar visualmente a estrutura estática do sistema, agrupando as classes em seus respectivos pacotes para maior clareza.

Toda a diagramação foi feita utilizando a sintaxe **Mermaid**, uma ferramenta leve de "diagrama como código" que permite gerar e versionar modelos UML diretamente em formato de texto, facilitando a integração com a documentação em Markdown.

**DICA:** baixe o arquivo UML-SHA.pdf localmente para uma melhor visualização.

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
    git clone https://github.com/Rodriguesmath/Simulador-Hidrometro.git
    cd simulador-hidrometro
    ```

2.  **Estrutura do TXT**
    Na raiz do projeto, acesse config:
    ```    ===============================================
    Arquivo de Configuração do Simulador de Hidrômetro
    ===============================================
    --- Configurações Gerais da Simulação ---
    Bitola do hidrômetro em polegadas.
    Opções válidas: 1/2, 3/4, 1, 1 1/2, 2, 3, 4
    bitola = 3/4

    Duração total da simulação em segundos.
    Ex: 86400 para simular 24 horas. Use -1 para execução infinita.
    tempoExecucao = -1

    --- Controle de Velocidade da Simulação ---
    Intervalo de atualização visual (em milissegundos).
    Controla a pausa no mundo real entre cada frame. 1000 equivale a 1 segundo.
    intervaloAtualizacao = 1000

    Escala de tempo (em segundos simulados por atualização).
    Controla quantos segundos o tempo da simulação avança a cada atualização visual.
    Ex: 1 para tempo real, 3600 para simular 1 hora a cada atualização.
    escalaDeTempo = 3600

    --- Parâmetros Físicos da Água ---
    Pressão mínima da água na rede (em bar)
    pressaoMinima = 3.0

    Pressão máxima da água na rede (em bar)
    pressaoMaxima = 6.0

    =================================================================
    Perfis de Consumo (Padrão Strategy)
    =================================================================
    Defina os horários (0-23) e as faixas de velocidade do fluxo (m/s)
    para cada período do dia. Os períodos não podem se sobrepor.
    =================================================================
    --- Perfil Madrugada (Consumo Baixo) ---
    madrugada_inicio = 0
    madrugada_fim = 5
    madrugada_vel_min = 1.5
    madrugada_vel_max = 2.8

    --- Perfil Manhã (Pico de Consumo) ---
    manha_inicio = 6
    manha_fim = 9
    manha_vel_min = 1.5
    manha_vel_max = 2.8

    --- Perfil Tarde (Consumo Moderado) ---
    tarde_inicio = 10
    tarde_fim = 17
    tarde_vel_min = 1.5
    tarde_vel_max = 2.8

    --- Perfil Noite (Segundo Pico) ---
    noite_inicio = 18
    noite_fim = 23
    noite_vel_min = 1.5
    noite_vel_max = 2.8

    --- Ativa (true) ou desativa (false) a simulação de passagem de ar quando a vazão for 0% ---
    simularAr=true

    --- Matrícula SUAP para nomear o diretório de medições (valor ficticio) ---
    matricula=199911250009
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
```plaintext
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
```
