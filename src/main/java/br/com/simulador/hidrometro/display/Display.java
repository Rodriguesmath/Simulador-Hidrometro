package main.java.br.com.simulador.hidrometro.display;

import main.java.br.com.simulador.config.SimulatorConfig;
import main.java.br.com.simulador.observer.Observador;
import main.java.br.com.simulador.hidrometro.ControleVazao;
import main.java.br.com.simulador.hidrometro.Medidor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Coordena a exibição e interação da interface gráfica do simulador.

 * Esta classe aplica dois padrões de projeto principais:
 * 1. **Facade (Fachada):** Simplifica a interação com um subsistema complexo (a UI).
 * Ela não executa as tarefas pesadas (desenhar, salvar, criar painéis), mas
 * delega essas responsabilidades para classes especialistas (`HidrometroRenderer`,
 * `ImagePersistenceService`, `ControleVazaoPanel`).
 * 2. **Observer (Observador):** Implementa a interface `Observador` para reagir
 * às atualizações da simulação de forma desacoplada, sendo notificada pelo
 * `HidrometroSimulator`.
 */
public class Display implements Observador {

    // --- Componentes da Janela Principal ---
    private final JFrame frame;
    private final JLabel labelImagem;

    // --- Especialistas para Delegação de Tarefas ---
    /** Referência ao objeto que sabe desenhar o hidrômetro. */
    private final HidrometroRenderer renderer;
    /** Referência ao serviço que sabe salvar imagens em segundo plano. */
    private final ImagePersistenceService imageSaver;

    /**
     * Estado interno para controlar a lógica de salvamento de imagens.
     * A imagem só é salva quando o valor inteiro de m³ muda.
     */
    private int ultimoM3Salvo = 0;

    /**
     * Constrói a janela da interface gráfica e inicializa seus componentes especialistas.
     * A assinatura deste construtor é mantida intencionalmente para garantir que
     * nenhuma classe que o utiliza precise ser modificada (Princípio do Aberto/Fechado).
     *
     * @param controleVazao Objeto de estado compartilhado para o controle de vazão.
     * @param config Objeto de configuração da simulação.
     */
    public Display(ControleVazao controleVazao, SimulatorConfig config) {
        // 1. Instancia os especialistas que farão o trabalho pesado.
        this.renderer = new HidrometroRenderer();
        this.imageSaver = new ImagePersistenceService(config);

        // 2. Configura a janela principal (o "contêiner" da UI).
        this.frame = new JFrame("Simulador de Hidrómetro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        // 3. Prepara a área onde a imagem do hidrômetro será exibida.
        this.labelImagem = new JLabel();
        frame.add(labelImagem, BorderLayout.CENTER);

        // 4. DELEGA a criação do complexo painel de controle para sua classe especialista.
        ControleVazaoPanel painelControle = new ControleVazaoPanel(controleVazao);
        frame.add(painelControle, BorderLayout.SOUTH);

        // Centraliza a janela na tela.
        frame.setLocationRelativeTo(null);
    }

    /**
     * Método chamado pelo `HidrometroSimulator` a cada passo da simulação.
     * Ele coordena a atualização da imagem na tela e aciona o salvamento quando necessário.
     *
     * @param medidor O estado atual do hidrômetro.
     * @param tempoTotalSimulado O tempo total decorrido na simulação.
     */
    @Override
    public void atualizar(Medidor medidor, int tempoTotalSimulado) {
        float totalM3 = medidor.getM3();

        // --- Orquestração das Tarefas de Atualização ---

        // 1. DELEGA a tarefa complexa de desenhar a imagem para o especialista em renderização.
        BufferedImage imagem = renderer.gerarImagemAnalogica(totalM3);
        // Atualiza a imagem exibida na tela.
        labelImagem.setIcon(new ImageIcon(imagem));

        // 2. Executa a lógica de coordenação: decide se é hora de salvar a imagem.
        int m3AtualInteiro = (int) totalM3;
        if (m3AtualInteiro > this.ultimoM3Salvo) {
            this.ultimoM3Salvo = m3AtualInteiro;

            // 3. DELEGA a tarefa de salvar o arquivo para o especialista em persistência.
            // Isso acontece em uma thread separada, não travando a interface.
            imageSaver.salvarImagem(imagem, m3AtualInteiro);
        }

        // Garante que a janela se torne visível na primeira chamada do método `atualizar`.
        if (!frame.isVisible()) {
            frame.pack(); // Ajusta o tamanho da janela ao conteúdo.
            frame.setVisible(true);
        }
    }

    /**
     * Método chamado pelo `HidrometroSimulator` quando a simulação termina.
     *
     * @param medidor O estado final do hidrômetro.
     */
    @Override
    public void simulacaoFinalizada(Medidor medidor) {
        // Atualiza o título da janela para dar feedback visual ao usuário.
        frame.setTitle("Simulador de Hidrómetro (FINALIZADO)");

        // DELEGA a tarefa de encerrar o serviço de thread para o especialista.
        // Isso é crucial para que a aplicação termine de forma limpa, sem threads "penduradas".
        imageSaver.shutdown();
    }
}