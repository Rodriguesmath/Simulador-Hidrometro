package main.java.br.com.simulador.hidrometro;

import main.java.br.com.simulador.observer.Observador;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Responsável por gerar a interface gráfica do hidrômetro.
 * Implementa a interface Observador para ser notificado sobre atualizações.
 */
public class Display implements Observador {

    private final JFrame frame;
    private final JLabel label;

    // --- Constantes de Dimensão e Posição ---
    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;
    private static final int CENTER_X = WIDTH / 2;
    private static final int CENTER_Y = HEIGHT / 2;
    private static final int RAIO_EXTERNO = 200;
    private static final int RAIO_INTERNO = 175;

    // --- Constantes de Cor ---
    private static final Color COR_FUNDO = Color.WHITE;
    private static final Color COR_CORPO_GRADIENTE_INICIO = new Color(65, 110, 180);
    private static final Color COR_CORPO_GRADIENTE_FIM = new Color(15, 40, 90);
    private static final Color COR_CARCACA_BASE = new Color(40, 40, 40);
    private static final Color COR_CARCACA_DESTAQUE = new Color(70, 70, 70);
    private static final Color COR_CARCACA_SOMBRA = new Color(0, 0, 0, 100);
    private static final Color COR_VISOR_FUNDO = new Color(220, 220, 220);
    private static final Color COR_VISOR_BORDA = new Color(180, 180, 180);
    private static final Color COR_DIGITO_PRETO = Color.BLACK;
    private static final Color COR_DIGITO_VERMELHO = Color.RED;
    private static final Color COR_PONTEIRO = Color.RED;
    private static final Color COR_SOMBRA_PONTEIRO = new Color(0, 0, 0, 70);

    // --- Constantes de Fonte ---
    private static final Font FONTE_VISOR_DIGITAL = new Font("Arial", Font.BOLD, 30);
    private static final Font FONTE_UNIDADE_M3 = new Font("Arial", Font.BOLD, 18);
    private static final Font FONTE_INFO_LABEL = new Font("Arial", Font.BOLD, 13);
    private static final Font FONTE_INFO_VALOR = new Font("Arial", Font.PLAIN, 11);
    private static final Font FONTE_SELO = new Font("Arial", Font.BOLD, 20);
    private static final Font FONTE_MOSTRADOR_LABEL = new Font("Arial", Font.BOLD, 11);
    private static final Font FONTE_MOSTRADOR_NUMERO = new Font("Arial", Font.BOLD, 12);


    public Display() {
        frame = new JFrame("Simulador de Hidrômetro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        label = new JLabel();
        frame.add(label);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void atualizar(Medidor medidor, int tempoSimulado) {
        BufferedImage img = gerarImagemHidrometro(medidor.getM3());
        label.setIcon(new ImageIcon(img));

        if (!frame.isVisible()) {
            frame.pack();
            frame.setVisible(true);
        }
    }

    @Override
    public void simulacaoFinalizada(Medidor estadoFinal) {
        // Neste observador, poderíamos, por exemplo, mudar o título da janela.
        frame.setTitle("Simulador de Hidrômetro - FINALIZADO");
    }

    private BufferedImage gerarImagemHidrometro(float totalM3) {
        BufferedImage imagem = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) imagem.getGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(COR_FUNDO);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        desenharCorpo(g);
        desenharCarcacaPrincipal(g);
        desenharVisorDigital(g, totalM3);
        desenharInformacoesEstaticas(g);
        desenharElementosCentrais(g, totalM3);

        g.dispose();
        return imagem;
    }

    private void desenharCorpo(Graphics2D g) {
        int corpoAlturaCentro = 300;
        int corpoAlturaBorda = 260;
        int conexaoAltura = 150;

        GeneralPath body = new GeneralPath();
        body.moveTo(0, CENTER_Y - conexaoAltura / 2.0);
        body.lineTo(CENTER_X - RAIO_EXTERNO + 40, CENTER_Y - corpoAlturaBorda / 2.0);
        body.quadTo(CENTER_X, CENTER_Y - corpoAlturaCentro / 2.0, CENTER_X + RAIO_EXTERNO - 40, CENTER_Y - corpoAlturaBorda / 2.0);
        body.lineTo(WIDTH, CENTER_Y - conexaoAltura / 2.0);
        body.lineTo(WIDTH, CENTER_Y + conexaoAltura / 2.0);
        body.lineTo(CENTER_X + RAIO_EXTERNO - 40, CENTER_Y + corpoAlturaBorda / 2.0);
        body.quadTo(CENTER_X, CENTER_Y + corpoAlturaCentro / 2.0, CENTER_X - RAIO_EXTERNO + 40, CENTER_Y + corpoAlturaBorda / 2.0);
        body.lineTo(0, CENTER_Y + conexaoAltura / 2.0);
        body.closePath();

        GradientPaint gpBody = new GradientPaint(0, CENTER_Y - corpoAlturaCentro / 2.0f, COR_CORPO_GRADIENTE_INICIO,
                0, CENTER_Y + corpoAlturaCentro / 2.0f, COR_CORPO_GRADIENTE_FIM);
        g.setPaint(gpBody);
        g.fill(body);
    }

    private void desenharCarcacaPrincipal(Graphics2D g) {
        g.setColor(COR_CARCACA_BASE);
        g.fillOval(CENTER_X - RAIO_EXTERNO, CENTER_Y - RAIO_EXTERNO, 2 * RAIO_EXTERNO, 2 * RAIO_EXTERNO);

        g.setColor(COR_CARCACA_DESTAQUE);
        g.setStroke(new BasicStroke(2));
        g.drawArc(CENTER_X - RAIO_EXTERNO + 2, CENTER_Y - RAIO_EXTERNO + 2, 2 * RAIO_EXTERNO - 4, 2 * RAIO_EXTERNO - 4, 45, 180);

        g.setColor(COR_CARCACA_SOMBRA);
        g.drawArc(CENTER_X - RAIO_EXTERNO + 2, CENTER_Y - RAIO_EXTERNO + 2, 2 * RAIO_EXTERNO - 4, 2 * RAIO_EXTERNO - 4, 225, 180);

        Point2D center = new Point2D.Float(CENTER_X - 60, CENTER_Y - 60);
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(255, 255, 255, 40), new Color(255, 255, 255, 0)};
        RadialGradientPaint rgp = new RadialGradientPaint(center, RAIO_EXTERNO, dist, colors);
        g.setPaint(rgp);
        g.fillOval(CENTER_X - RAIO_EXTERNO, CENTER_Y - RAIO_EXTERNO, 2 * RAIO_EXTERNO, 2 * RAIO_EXTERNO);

        g.setColor(Color.WHITE);
        g.fillOval(CENTER_X - RAIO_INTERNO, CENTER_Y - RAIO_INTERNO, 2 * RAIO_INTERNO, 2 * RAIO_INTERNO);
        g.setColor(new Color(0, 0, 0, 50));
        g.setStroke(new BasicStroke(3));
        g.drawArc(CENTER_X - RAIO_INTERNO, CENTER_Y - RAIO_INTERNO, 2 * RAIO_INTERNO, 2 * RAIO_INTERNO, 225, 180);
        g.setColor(new Color(255, 255, 255, 40));
        g.setStroke(new BasicStroke(2));
        g.drawArc(CENTER_X - RAIO_INTERNO, CENTER_Y - RAIO_INTERNO, 2 * RAIO_INTERNO, 2 * RAIO_INTERNO, 45, 180);
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(1));
        g.drawOval(CENTER_X - RAIO_INTERNO, CENTER_Y - RAIO_INTERNO, 2 * RAIO_INTERNO, 2 * RAIO_INTERNO);
    }

    private void desenharVisorDigital(Graphics2D g, float totalM3) {
        int visorWidth = 28, visorHeight = 38, visorY = CENTER_Y - 75, gap = 4;
        int totalDisplayWidth = 6 * visorWidth + 5 * gap;
        int startX = CENTER_X - totalDisplayWidth / 2;

        g.setColor(COR_VISOR_FUNDO);
        g.fill(new RoundRectangle2D.Float(startX - 3, visorY - 3, totalDisplayWidth + 6, visorHeight + 6, 8, 8));
        g.setColor(COR_VISOR_BORDA);
        g.draw(new RoundRectangle2D.Float(startX - 3, visorY - 3, totalDisplayWidth + 6, visorHeight + 6, 8, 8));

        int m3Inteiro = (int) totalM3 % 10000;
        String m3Str = String.format("%04d", m3Inteiro);

        for (int i = 0; i < 4; i++) {
            int visorX = startX + i * (visorWidth + gap);
            desenharDigito(g, String.valueOf(m3Str.charAt(i)), visorX, visorY, visorWidth, visorHeight, COR_DIGITO_PRETO);
        }

        int centenasLitros = (int) ((totalM3 * 10) % 10);
        int dezenasLitros = (int) ((totalM3 * 100) % 10);
        int[] digitosVermelhos = {centenasLitros, dezenasLitros};

        for (int i = 0; i < 2; i++) {
            int visorX = startX + (4 + i) * (visorWidth + gap);
            desenharDigito(g, String.valueOf(digitosVermelhos[i]), visorX, visorY, visorWidth, visorHeight, COR_DIGITO_VERMELHO);
        }

        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(0.5f));
        for (int i = 1; i < 6; i++) {
            int lineX = startX + i * visorWidth + (i - 1) * gap + gap / 2;
            g.drawLine(lineX, visorY, lineX, visorY + visorHeight);
        }

        g.setFont(FONTE_UNIDADE_M3);
        g.setColor(COR_DIGITO_PRETO);
        g.drawString("m³", startX + totalDisplayWidth + 8, visorY + 28);
    }

    private void desenharDigito(Graphics2D g, String texto, int x, int y, int w, int h, Color cor) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, w, h);
        g.setColor(cor);
        g.setFont(FONTE_VISOR_DIGITAL);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(texto);
        g.drawString(texto, x + (w - textWidth) / 2, y + 29);
    }

    private void desenharInformacoesEstaticas(Graphics2D g) {
        int infoX = CENTER_X - 145;
        g.setFont(FONTE_INFO_LABEL);
        g.setColor(COR_DIGITO_PRETO);
        g.drawString("H-B", infoX, CENTER_Y - 8);
        g.drawString("V-A", infoX, CENTER_Y + 10);
        g.setFont(FONTE_INFO_VALOR);
        g.drawString("Qn: 1,5 m³/h", infoX, CENTER_Y + 30);
        g.drawString("Qmin: 0,030 m³/h", infoX, CENTER_Y + 45);

        int seloX = infoX;
        int seloY = CENTER_Y + 60;
        int seloDiametro = 38;

        g.setColor(COR_DIGITO_PRETO);
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(seloX, seloY, seloDiametro, seloDiametro);
        g.setFont(FONTE_SELO);
        g.drawString("N", seloX + 11, seloY + 27);
        g.setStroke(new BasicStroke(2.0f));
        g.drawLine(seloX + 9, seloY + 29, seloX + 26, seloY + 11);
    }

    private void desenharElementosCentrais(Graphics2D g, float totalM3) {
        int circuloCentralX = CENTER_X;
        int circuloCentralY = CENTER_Y + 15;
        int circuloRaio = 16;

        g.setColor(COR_DIGITO_PRETO);
        g.fillArc(circuloCentralX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 0, 90);
        g.fillArc(circuloCentralX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 180, 90);
        g.setColor(Color.WHITE);
        g.fillArc(circuloCentralX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 90, 90);
        g.fillArc(circuloCentralX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 270, 90);
        g.setColor(COR_DIGITO_PRETO);
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(circuloCentralX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2);

        int raioMostrador = 42;
        int litrosCenterX = CENTER_X + 85;
        int litrosCenterY = CENTER_Y + 65;
        int decimosCenterX = CENTER_X;
        int decimosCenterY = CENTER_Y + 125;

        float litrosValor = (totalM3 * 1000f) % 10f;
        desenharMostrador(g, litrosCenterX, litrosCenterY, raioMostrador, litrosValor);
        float decimosValor = (totalM3 * 10000f) % 10f;
        desenharMostrador(g, decimosCenterX, decimosCenterY, raioMostrador, decimosValor);
    }

    private void desenharMostrador(Graphics2D g, int centerX, int centerY, int radius, float value) {
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(2));
        g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

        g.setFont(FONTE_MOSTRADOR_NUMERO);
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

        g.setColor(COR_SOMBRA_PONTEIRO);
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(centerX + 1, centerY + 1, pointerEndX + 1, pointerEndY + 1);

        g.setColor(COR_PONTEIRO);
        g.drawLine(centerX, centerY, pointerEndX, pointerEndY);
        g.fillOval(centerX - 3, centerY - 3, 6, 6);
    }
}

