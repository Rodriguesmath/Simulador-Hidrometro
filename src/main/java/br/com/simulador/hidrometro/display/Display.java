package main.java.br.com.simulador.hidrometro.display;

import main.java.br.com.simulador.config.SimulatorConfig;
import main.java.br.com.simulador.observer.Observador;
import main.java.br.com.simulador.hidrometro.ControleVazao;
import main.java.br.com.simulador.hidrometro.Medidor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Responsabilidade Única: COORDENAR a exibição da interface gráfica.
 * Atua como uma "Fachada" (Facade), delegando as tarefas para classes especialistas.
 * Implementa a interface Observador para ser notificado sobre atualizações.
 */
public class Display implements Observador {

    private final JFrame frame;
    private final JLabel labelImagem;

    // Armazena referências para os especialistas
    private final HidrometroRenderer renderer;
    private final ImagePersistenceService imageSaver;

    private int ultimoM3Salvo = 0;

    // O construtor MANTÉM A MESMA ASSINATURA PÚBLICA. Nenhuma classe que usa Display precisa ser modificada.
    public Display(ControleVazao controleVazao, SimulatorConfig config) {
        // Instancia os especialistas que farão o trabalho pesado
        this.renderer = new HidrometroRenderer();
        this.imageSaver = new ImagePersistenceService(config);

        // Configuração da janela principal
        this.frame = new JFrame("Simulador de Hidrómetro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        this.labelImagem = new JLabel();
        frame.add(labelImagem, BorderLayout.CENTER);

        // DELEGA a criação do painel para a classe especialista
        ControleVazaoPanel painelControle = new ControleVazaoPanel(controleVazao);
        frame.add(painelControle, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
    }

    @Override
    public void atualizar(Medidor medidor, int tempoTotalSimulado) {
        float totalM3 = medidor.getM3();

        // 1. DELEGA a renderização para o especialista
        BufferedImage imagem = renderer.gerarImagemAnalogica(totalM3);
        labelImagem.setIcon(new ImageIcon(imagem));

        // 2. Controla a lógica de QUANDO salvar
        int m3AtualInteiro = (int) totalM3;
        if (m3AtualInteiro > this.ultimoM3Salvo) {
            this.ultimoM3Salvo = m3AtualInteiro;

            // 3. DELEGA o salvamento da imagem para o especialista
            imageSaver.salvarImagem(imagem, m3AtualInteiro);
        }

        if (!frame.isVisible()) {
            frame.pack();
            frame.setVisible(true);
        }
    }

    @Override
    public void simulacaoFinalizada(Medidor medidor) {
        frame.setTitle("Simulador de Hidrómetro (FINALIZADO)");

        // DELEGA o encerramento da thread para o especialista
        imageSaver.shutdown();
    }
}