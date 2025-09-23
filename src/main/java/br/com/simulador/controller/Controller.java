package main.java.br.com.simulador.controller;

import main.java.br.com.simulador.config.SimulatorConfig;
import main.java.br.com.simulador.hidrometro.ControleVazao;
import main.java.br.com.simulador.hidrometro.display.Display;
import main.java.br.com.simulador.hidrometro.HidrometroSimulator;
import main.java.br.com.simulador.hidrometro.Saida;

/**
 * Orquestra o ciclo de vida completo da aplicação de simulação.
 *
 * Esta classe atua como o "Maestro" ou ponto central da aplicação. Suas responsabilidades são:
 * 1. **Inicialização:** Criar e "conectar" (injetar dependências) todos os componentes principais do sistema.
 * Este ponto é conhecido como a "Raiz de Composição" (Composition Root) da aplicação.
 * 2. **Execução:** Gerenciar o loop principal da simulação, ditando o ritmo e o avanço do tempo.
 * 3. **Finalização:** Garantir que os componentes sejam notificados quando a simulação terminar.
 */
public class Controller {
    /** Referência para o objeto de configuração, usado em toda a simulação. */
    private final SimulatorConfig config;
    /** Referência para o motor principal da simulação. */
    private final HidrometroSimulator simulator;

    /**
     * Constrói o Controller, inicializando e interconectando todo o sistema.
     *
     * @param configPath O caminho do arquivo de configuração a ser carregado.
     */
    public Controller(String configPath) {
        // --- ETAPA 1: CARREGAR CONFIGURAÇÕES ---
        // Cria o objeto de configuração a partir do arquivo.
        // Nota: Em uma refatoração anterior, movemos esta lógica para uma classe 'ConfigLoader'.
        this.config = new SimulatorConfig(configPath);

        // --- ETAPA 2: CRIAR E CONECTAR OS COMPONENTES (COMPOSITION ROOT) ---

        // Cria a instância ÚNICA do objeto de estado que será compartilhado entre a UI e a simulação.
        // Ele funciona como a "ponte" entre o input do usuário (slider) e o cálculo do fluxo.
        ControleVazao controleVazao = new ControleVazao();

        // Cria o motor da simulação, injetando as dependências de que ele precisa.
        // Nota: Em uma refatoração posterior, injetamos uma 'EntradaFactory' em vez do 'ControleVazao'.
        this.simulator = new HidrometroSimulator(config, controleVazao);

        // Cria a View da interface gráfica (Display) e a registra como um "ouvinte" (Observador) do simulador.
        // É crucial que a mesma instância de 'controleVazao' seja passada para o Display.
        simulator.adicionarObservador(new Display(controleVazao, this.config));

        // Cria a View de saída de texto (Saida) e também a registra como um observador.
        simulator.adicionarObservador(new Saida(this.config));
    }

    /**
     * Inicia e gerencia o ciclo principal (loop) da simulação.
     * Este método só retorna quando a simulação for concluída ou interrompida.
     */
    public void startSimulacao() {
        // Notifica os observadores (Display, Saida) uma vez antes do loop começar.
        // Isso garante que o estado inicial (tempo = 0) seja exibido na tela e no console.
        simulator.notificarObservadores();

        // --- O LOOP PRINCIPAL DA APLICAÇÃO ---
        // O loop continua enquanto a condição de término da simulação (definida no config) não for atingida.
        while (!simulator.isFinalizado()) {
            // 1. Avança a simulação em um passo de tempo.
            // Esta chamada aciona a lógica no 'HidrometroSimulator', que por sua vez
            // atualiza o 'Medidor' e notifica todos os observadores (Display, Saida).
            simulator.avancarSimulacao();

            // 2. Pausa a execução para controlar a velocidade da VISUALIZAÇÃO.
            // Isso previne que a simulação execute rápido demais para ser acompanhada visualmente.
            // O tempo de pausa é definido no arquivo de configuração.
            try {
                Thread.sleep(config.getIntervaloAtualizacao());
            } catch (InterruptedException e) {
                // Boa prática: se a thread for interrompida, restaura o status de interrupção e para o loop.
                Thread.currentThread().interrupt();
                System.out.println("Simulação interrompida.");
                break;
            }
        }

        // --- FINALIZAÇÃO ---
        // Após o término do loop, notifica todos os observadores de que a simulação acabou.
        // Isso permite que eles executem ações de finalização (ex: mudar título da janela, exibir resumo).
        simulator.notificarFimSimulacao();

    }
}