package main.java.br.com.simulador.hidrometro.display;

import main.java.br.com.simulador.hidrometro.ControleVazao;
import javax.swing.*;
import java.awt.*;

/**
 * Responsabilidade Única: Criar e gerenciar o painel de controle de vazão.
 * É um componente de UI autônomo.
 */
public class ControleVazaoPanel extends JPanel {

    public ControleVazaoPanel(ControleVazao controleVazao) {
        // A lógica de getJPanel() vem para o construtor da superclasse
        super(new BorderLayout(10, 0));
        setBackground(new Color(30, 60, 100));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10), // Padding
                BorderFactory.createLineBorder(new Color(70, 100, 150), 2, true) // Contorno
        ));

        // O resto da lógica de criarPainelControle() vem para cá
        initComponents(controleVazao);
    }

    private void initComponents(ControleVazao controleVazao) {
        JLabel tituloLabel = new JLabel("Controle de Vazão (0-100%)", SwingConstants.CENTER);
        tituloLabel.setForeground(Color.WHITE);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(tituloLabel, BorderLayout.NORTH);

        JSlider sliderVazao = new JSlider(0, 100, 100);
        sliderVazao.setOpaque(false);
        UIManager.put("Slider.trackColor", new Color(70, 100, 150));
        UIManager.put("Slider.thumbColor", new Color(180, 200, 220));
        add(sliderVazao, BorderLayout.CENTER);

        JLabel labelValorVazao = new JLabel("100%", SwingConstants.CENTER);
        labelValorVazao.setFont(new Font("Arial", Font.BOLD, 16));
        labelValorVazao.setForeground(new Color(230, 230, 230));

        sliderVazao.addChangeListener(e -> {
            int percentual = sliderVazao.getValue();
            controleVazao.setMultiplicador(percentual);
            labelValorVazao.setText(percentual + "%");
        });

        JPanel valorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        valorPanel.setOpaque(false);
        valorPanel.add(labelValorVazao);
        add(valorPanel, BorderLayout.EAST);
    }
}
