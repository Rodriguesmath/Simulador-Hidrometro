package main.java.br.com.simulador.hidrometro;

import main.java.br.com.simulador.observer.Observador;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Responsável por gerar a interface gráfica do hidrómetro e o controlo de vazão.
 * Implementa a interface Observador para ser notificado sobre atualizações.
 */
public class Display implements Observador {

    private final JFrame frame;
    private final JLabel labelImagem;
    private final ControleVazao controleVazao; // 1. Campo para guardar a referência

    public Display(ControleVazao controleVazao) { // 2. Aceitar o objeto no construtor
        this.controleVazao = controleVazao;

        frame = new JFrame("Simulador de Hidrómetro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout()); // Mudar o layout para acomodar o painel de controlo

        labelImagem = new JLabel();
        frame.add(labelImagem, BorderLayout.CENTER);

        // 3. Adicionar o painel de controlo com o slider
        frame.add(criarPainelControle(), BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
    }

    /**
     * Cria o painel com o slider para controlar a vazão em tempo real.
     */
    private JPanel criarPainelControle() {
        JPanel painel = new JPanel(new BorderLayout(10, 0));

        // 1. Fundo do Painel: Azul escuro para harmonizar com o design do hidrômetro.
        painel.setBackground(new Color(30, 60, 100));

        // 2. Borda: Remoção do TitledBorder e uso de uma borda com cantos arredondados.
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10), // Padding
                BorderFactory.createLineBorder(new Color(70, 100, 150), 2, true) // Contorno azul arredondado
        ));

        // Adiciona o título como um JLabel separado, com cor e fonte personalizadas
        JLabel tituloLabel = new JLabel("Controle de Vazão (0-100%)", SwingConstants.CENTER);
        tituloLabel.setForeground(Color.WHITE);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 14));
        painel.add(tituloLabel, BorderLayout.NORTH);

        // Slider de 0 a 100 (percentual)
        JSlider sliderVazao = new JSlider(0, 100, 100);

        // 3. Slider Personalizado: Torna-o transparente para que o fundo do painel seja visível
        // e usa cores para a trilha e o indicador.
        sliderVazao.setOpaque(false);

        // Configura as cores do slider (isso afeta todos os sliders, idealmente seria um UI personalizado)
        UIManager.put("Slider.trackColor", new Color(70, 100, 150));
        UIManager.put("Slider.thumbColor", new Color(180, 200, 220));

        // Opcional: Adiciona um JComponent vazio para servir de "espaço" para a aparência do slider, se necessário.
        painel.add(sliderVazao, BorderLayout.CENTER);

        // Label do valor da vazão
        JLabel labelValorVazao = new JLabel("100%", SwingConstants.CENTER);
        labelValorVazao.setFont(new Font("Arial", Font.BOLD, 16));
        // 4. Cor do Texto: Cinza claro para bom contraste.
        labelValorVazao.setForeground(new Color(230, 230, 230));

        sliderVazao.addChangeListener(e -> {
            int percentual = sliderVazao.getValue();

            // Atualiza o objeto compartilhado
            // Note: A variável 'controleVazao' deve ser acessível dentro desta classe.
            controleVazao.setMultiplicador(percentual);

            // Atualiza o texto na interface
            labelValorVazao.setText(percentual + "%");
        });

        // Adiciona o valor em um painel auxiliar para um melhor controle de alinhamento
        JPanel valorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        valorPanel.setOpaque(false);
        valorPanel.add(labelValorVazao);

        painel.add(valorPanel, BorderLayout.EAST);

        return painel;
    }


    @Override
    public void atualizar(Medidor medidor, int tempoTotalSimulado) {
        float totalM3 = medidor.getM3();
        BufferedImage imagem = gerarImagemAnalogica(totalM3);
        labelImagem.setIcon(new ImageIcon(imagem));

        if (!frame.isVisible()) {
            frame.pack();
            frame.setVisible(true);
        }
    }

    @Override
    public void simulacaoFinalizada(Medidor medidor) {
        frame.setTitle("Simulador de Hidrómetro (FINALIZADO)");
    }

    // O método gerarImagemAnalogica(...) e os seus métodos auxiliares permanecem os mesmos.
    // O código foi omitido para facilitar a leitura, mas continua presente no seu ficheiro.
    private BufferedImage gerarImagemAnalogica(float total_m3) {
        // ...código de desenho sem alterações...
        int width = 800, height = 500;
        BufferedImage imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) imagem.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        int centroX = width / 2;
        int centroY = height / 2;

        int raioExterno = 200;
        int raioInterno = 175;

        // --- CORPO AZUL COM GRADIENTE PARA EFEITO 3D ---
        int corpoAlturaCentro = 300;
        int corpoAlturaBorda = 260;
        int conexaoAltura = 150;

        GeneralPath body = new GeneralPath();
        body.moveTo(0, centroY - conexaoAltura / 2.0);
        body.lineTo(centroX - raioExterno + 40, centroY - corpoAlturaBorda / 2.0);
        body.quadTo(centroX, centroY - corpoAlturaCentro / 2.0, centroX + raioExterno - 40, centroY - corpoAlturaBorda / 2.0);
        body.lineTo(width, centroY - conexaoAltura / 2.0);
        body.lineTo(width, centroY + conexaoAltura / 2.0);
        body.lineTo(centroX + raioExterno - 40, centroY + corpoAlturaBorda / 2.0);
        body.quadTo(centroX, centroY + corpoAlturaCentro / 2.0, centroX - raioExterno + 40, centroY + corpoAlturaBorda / 2.0);
        body.lineTo(0, centroY + conexaoAltura / 2.0);
        body.closePath();

        GradientPaint gpBody = new GradientPaint(0, centroY - corpoAlturaCentro / 2.0f, new Color(65, 110, 180),
                0, centroY + corpoAlturaCentro / 2.0f, new Color(15, 40, 90));
        g.setPaint(gpBody);
        g.fill(body);

        // --- Carcaça preta com efeito de bisel (chanfro) ---
        Color basePreto = new Color(40, 40, 40);
        g.setColor(basePreto);
        g.fillOval(centroX - raioExterno, centroY - raioExterno, 2 * raioExterno, 2 * raioExterno);

        // Destaque superior do bisel EXTERNO
        g.setColor(new Color(70, 70, 70));
        g.setStroke(new BasicStroke(2));
        g.drawArc(centroX - raioExterno + 2, centroY - raioExterno + 2, 2 * raioExterno - 4, 2 * raioExterno - 4, 45, 180);

        // Sombra inferior do bisel EXTERNO
        g.setColor(new Color(0, 0, 0, 100));
        g.drawArc(centroX - raioExterno + 2, centroY - raioExterno + 2, 2 * raioExterno - 4, 2 * raioExterno - 4, 225, 180);

        // Adiciona um reflexo radial suave por cima
        Point2D center = new Point2D.Float(centroX - 60, centroY - 60);
        float radius = raioExterno;
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(255, 255, 255, 40), new Color(255, 255, 255, 0)};
        RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, colors);
        g.setPaint(rgp);
        g.fillOval(centroX - raioExterno, centroY - raioExterno, 2 * raioExterno, 2 * raioExterno);

        // Visor interno branco
        g.setColor(Color.WHITE);
        g.fillOval(centroX - raioInterno, centroY - raioInterno, 2 * raioInterno, 2 * raioInterno);

        // --- EFEITO INTERNO DO BISEL (SOBRE O VISOR BRANCO) ---
        // Sombra interna (sombra projetada no visor branco)
        g.setColor(new Color(0, 0, 0, 50));
        g.setStroke(new BasicStroke(3));
        g.drawArc(centroX - raioInterno, centroY - raioInterno, 2 * raioInterno, 2 * raioInterno, 225, 180);

        // Destaque interno (luz batendo na borda interna)
        g.setColor(new Color(255, 255, 255, 40));
        g.setStroke(new BasicStroke(2));
        g.drawArc(centroX - raioInterno, centroY - raioInterno, 2 * raioInterno, 2 * raioInterno, 45, 180);


        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(1));
        g.drawOval(centroX - raioInterno, centroY - raioInterno, 2 * raioInterno, 2 * raioInterno);

        // --- VISOR DIGITAL ---
        int visorWidth = 28, visorHeight = 38, visorY = centroY - 75, gap = 4;
        int totalDisplayWidth = 6 * visorWidth + 5 * gap;
        int startX = centroX - totalDisplayWidth / 2;
        Font fontDigital = new Font("Arial", Font.BOLD, 30);
        g.setFont(fontDigital);

        g.setColor(new Color(220, 220, 220));
        g.fill(new RoundRectangle2D.Float(startX - 3, visorY - 3, totalDisplayWidth + 6, visorHeight + 6, 8, 8));
        g.setColor(new Color(180, 180, 180));
        g.draw(new RoundRectangle2D.Float(startX - 3, visorY - 3, totalDisplayWidth + 6, visorHeight + 6, 8, 8));


        int m3Inteiro = (int) total_m3;
        m3Inteiro = m3Inteiro % 10000;
        String m3Str = String.format("%04d", m3Inteiro);
        for (int i = 0; i < 4; i++) {
            int visorX = startX + i * (visorWidth + gap);
            g.setColor(Color.WHITE);
            g.fillRect(visorX, visorY, visorWidth, visorHeight);
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(String.valueOf(m3Str.charAt(i)));
            g.drawString(String.valueOf(m3Str.charAt(i)), visorX + (visorWidth - textWidth) / 2, visorY + 29);
        }
        int centenasLitros = (int) ((total_m3 * 10) % 10);
        int dezenasLitros = (int) ((total_m3 * 100) % 10);
        int[] digitosVermelhos = {centenasLitros, dezenasLitros};
        for (int i = 0; i < 2; i++) {
            int visorX = startX + (4 + i) * (visorWidth + gap);
            g.setColor(Color.WHITE);
            g.fillRect(visorX, visorY, visorWidth, visorHeight);
            g.setColor(Color.RED);
            String digito = String.valueOf(digitosVermelhos[i]);
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(digito);
            g.drawString(digito, visorX + (visorWidth - textWidth) / 2, visorY + 29);
        }

        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(0.5f));
        for (int i = 1; i < 6; i++) {
            int lineX = startX + i * visorWidth + (i - 1) * gap + gap / 2;
            g.drawLine(lineX, visorY, lineX, visorY + visorHeight);
        }


        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.BLACK);
        g.drawString("m³", startX + totalDisplayWidth + 8, visorY + 28);

        // --- Textos e Selo ---
        int infoX = centroX - 145;
        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString("H-B", infoX, centroY - 8);
        g.drawString("V-A", infoX, centroY + 10);
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("Qn: 1,5 m³/h", infoX, centroY + 30);
        g.drawString("Qmin: 0,030 m³/h", infoX, centroY + 45);

        int seloX = infoX;
        int seloY = centroY + 60;
        int seloDiametro = 38;

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(seloX, seloY, seloDiametro, seloDiametro);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("N", seloX + 11, seloY + 27);
        g.setStroke(new BasicStroke(2.0f));
        g.drawLine(seloX + 9, seloY + 29, seloX + 26, seloY + 11);

        // --- Elemento Central (indicador de fluxo) ---
        int circuloCentralX = centroX;
        int circuloCentralY = centroY + 15;
        int circuloRaio = 16;
        g.setColor(Color.BLACK);
        g.fillArc(circuloCentralX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 0, 90);
        g.fillArc(circuloCentralX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 180, 90);
        g.setColor(Color.WHITE);
        g.fillArc(circuloCentralX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 90, 90);
        g.fillArc(circuloCentralX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 270, 90);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(circuloCentralX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2);

        // --- Relógios Analógicos ---
        int raioMostrador = 42;
        int litrosCenterX = centroX + 85;
        int litrosCenterY = centroY + 65;
        int decimosCenterX = centroX;
        int decimosCenterY = centroY + 125;

        float litrosValor = (total_m3 * 1000f) % 10f;
        desenharMostrador(g, "", litrosCenterX, litrosCenterY, raioMostrador, litrosValor);
        float decimosValor = (total_m3 * 10000f) % 10f;
        desenharMostrador(g, "", decimosCenterX, decimosCenterY, raioMostrador, decimosValor);

        g.dispose();
        return imagem;
    }

    private void desenharMostrador(Graphics2D g, String label, int centerX, int centerY, int radius, float value) {
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(2));
        g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
        g.setFont(new Font("Arial", Font.BOLD, 11));
        FontMetrics fmLabel = g.getFontMetrics();
        int labelWidth = fmLabel.stringWidth(label);
        g.drawString(label, centerX - labelWidth / 2, centerY - radius - 8);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        for (int i = 0; i < 10; i++) {
            double angle = Math.toRadians(i * 36 - 90);
            int numX = centerX + (int) ((radius - 11) * Math.cos(angle));
            int numY = centerY + (int) ((radius - 11) * Math.sin(angle));
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(String.valueOf(i));
            g.drawString(String.valueOf(i), numX - textWidth / 2, numY + 5);
        }
        double angle = Math.toRadians((value * 36) - 90);
        int pointerEndX = centerX + (int) ((radius - 8) * Math.cos(angle));
        int pointerEndY = centerY + (int) ((radius - 8) * Math.sin(angle));

        // Sombra do ponteiro
        g.setColor(new Color(0,0,0,70));
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(centerX + 1, centerY + 1, pointerEndX + 1, pointerEndY + 1);

        // Ponteiro
        g.setColor(Color.RED);
        g.drawLine(centerX, centerY, pointerEndX, pointerEndY);
        g.fillOval(centerX - 3, centerY - 3, 6, 6);
    }
}

