package main.java.br.com.simulador.hidrometro.display;

import main.java.br.com.simulador.hidrometro.ControleVazao;
import javax.swing.*;
import java.awt.*;

/**
 * Representa um componente de UI (painel) autônomo e reutilizável,
 * cuja única responsabilidade é fornecer um controle deslizante (JSlider)
 * para que o usuário possa ajustar a vazão da simulação em tempo real.
 */
public class ControleVazaoPanel extends JPanel {

    /**
     * Constrói e inicializa o painel de controle de vazão.
     * @param controleVazao Uma instância do objeto de estado compartilhado.
     * Este painel irá modificar o estado deste objeto
     * quando o usuário interagir com o slider.
     */
    public ControleVazaoPanel(ControleVazao controleVazao) {
        // Define o gerenciador de layout principal do painel como BorderLayout.
        // Os valores 10 e 0 definem espaçamentos horizontal e vertical entre os componentes.
        super(new BorderLayout(10, 0));

        // Define a aparência base do painel (fundo e borda).
        // Estas configurações visam uma harmonia visual com o restante da interface.
        setBackground(new Color(30, 60, 100));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10), // Adiciona um preenchimento (padding) interno.
                BorderFactory.createLineBorder(new Color(70, 100, 150), 2, true) // Adiciona uma borda azulada e arredondada.
        ));

        // Delega a criação e adição dos componentes internos para um método separado,
        // mantendo o construtor mais limpo e organizado.
        initComponents(controleVazao);
    }

    /**
     * Cria e posiciona todos os componentes de UI (título, slider, label de valor)
     * dentro do painel.
     * @param controleVazao O objeto de estado a ser atualizado pelo slider.
     */
    private void initComponents(ControleVazao controleVazao) {
        // --- 1. Título do Painel ---
        JLabel tituloLabel = new JLabel("Controle de Vazão (0-100%)", SwingConstants.CENTER);
        tituloLabel.setForeground(Color.WHITE); // Cor do texto.
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Fonte do texto.
        add(tituloLabel, BorderLayout.NORTH); // Adiciona o título na parte superior do painel.

        // --- 2. Slider (Controle Deslizante) ---
        // Cria um slider que vai de 0 a 100, com valor inicial 100.
        JSlider sliderVazao = new JSlider(0, 100, 100);

        // Torna o fundo do slider transparente para que a cor do painel principal apareça.
        sliderVazao.setOpaque(false);

        // ATENÇÃO: UIManager.put afeta TODOS os JSliders da aplicação.
        // Para um componente verdadeiramente isolado, o ideal seria criar uma UI customizada (SliderUI).
        // Aqui, para simplificar, definimos as cores de forma global.
        UIManager.put("Slider.trackColor", new Color(70, 100, 150)); // Cor da trilha do slider.
        UIManager.put("Slider.thumbColor", new Color(180, 200, 220)); // Cor do indicador (bolinha) do slider.

        add(sliderVazao, BorderLayout.CENTER); // Adiciona o slider na área central do painel.

        // --- 3. Label de Exibição do Valor ---
        JLabel labelValorVazao = new JLabel("100%", SwingConstants.CENTER);
        labelValorVazao.setFont(new Font("Arial", Font.BOLD, 16));
        labelValorVazao.setForeground(new Color(230, 230, 230));

        // --- 4. Lógica de Interação (O Coração do Componente) ---
        // Adiciona um "ouvinte" que será acionado toda vez que o valor do slider mudar.
        sliderVazao.addChangeListener(e -> {
            // Pega o valor atual do slider (um inteiro entre 0 e 100).
            int percentual = sliderVazao.getValue();

            // PONTO CRÍTICO: Atualiza o objeto de estado compartilhado (ControleVazao).
            // É assim que este componente de UI comunica a mudança para o resto do sistema (a simulação).
            controleVazao.setMultiplicador(percentual);

            // Atualiza o texto do label na tela para dar feedback visual imediato ao usuário.
            labelValorVazao.setText(percentual + "%");
        });

        // --- 5. Painel Auxiliar para Alinhamento ---
        // Usa um painel extra com FlowLayout para garantir que o label de valor
        // fique bem posicionado dentro da área LESTE (EAST) do BorderLayout principal.
        JPanel valorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        valorPanel.setOpaque(false); // Transparente para não cobrir o fundo principal.
        valorPanel.add(labelValorVazao);
        add(valorPanel, BorderLayout.EAST); // Adiciona o painel auxiliar na parte direita.
    }
}