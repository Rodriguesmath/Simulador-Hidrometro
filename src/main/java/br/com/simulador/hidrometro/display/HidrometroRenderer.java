package main.java.br.com.simulador.hidrometro.display;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Responsabilidade Única: Gerar a imagem (BufferedImage) de um hidrômetro
 * com base no valor de consumo em m³. É uma classe puramente de desenho.
 */
public class HidrometroRenderer {

    public BufferedImage gerarImagemAnalogica(float total_m3) {
        // ...código de desenho sem alterações...
        int width = 800, height = 500;
        BufferedImage imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) imagem.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        int decimosCenterX = width / 2;
        int centroY = height / 2;

        int raioExterno = 200;
        int raioInterno = 175;

        // --- CORPO AZUL COM GRADIENTE PARA EFEITO 3D ---
        int corpoAlturaCentro = 300;
        int corpoAlturaBorda = 260;
        int conexaoAltura = 150;

        GeneralPath body = new GeneralPath();
        body.moveTo(0, centroY - conexaoAltura / 2.0);
        body.lineTo(decimosCenterX - raioExterno + 40, centroY - corpoAlturaBorda / 2.0);
        body.quadTo(decimosCenterX, centroY - corpoAlturaCentro / 2.0, decimosCenterX + raioExterno - 40, centroY - corpoAlturaBorda / 2.0);
        body.lineTo(width, centroY - conexaoAltura / 2.0);
        body.lineTo(width, centroY + conexaoAltura / 2.0);
        body.lineTo(decimosCenterX + raioExterno - 40, centroY + corpoAlturaBorda / 2.0);
        body.quadTo(decimosCenterX, centroY + corpoAlturaCentro / 2.0, decimosCenterX - raioExterno + 40, centroY + corpoAlturaBorda / 2.0);
        body.lineTo(0, centroY + conexaoAltura / 2.0);
        body.closePath();

        GradientPaint gpBody = new GradientPaint(0, centroY - corpoAlturaCentro / 2.0f, new Color(65, 110, 180),
                0, centroY + corpoAlturaCentro / 2.0f, new Color(15, 40, 90));
        g.setPaint(gpBody);
        g.fill(body);

        // --- Carcaça preta com efeito de bisel (chanfro) ---
        Color basePreto = new Color(40, 40, 40);
        g.setColor(basePreto);
        g.fillOval(decimosCenterX - raioExterno, centroY - raioExterno, 2 * raioExterno, 2 * raioExterno);

        // Destaque superior do bisel EXTERNO
        g.setColor(new Color(70, 70, 70));
        g.setStroke(new BasicStroke(2));
        g.drawArc(decimosCenterX - raioExterno + 2, centroY - raioExterno + 2, 2 * raioExterno - 4, 2 * raioExterno - 4, 45, 180);

        // Sombra inferior do bisel EXTERNO
        g.setColor(new Color(0, 0, 0, 100));
        g.drawArc(decimosCenterX - raioExterno + 2, centroY - raioExterno + 2, 2 * raioExterno - 4, 2 * raioExterno - 4, 225, 180);

        // Adiciona um reflexo radial suave por cima
        Point2D center = new Point2D.Float(decimosCenterX - 60, centroY - 60);
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(255, 255, 255, 40), new Color(255, 255, 255, 0)};
        RadialGradientPaint rgp = new RadialGradientPaint(center, (float) raioExterno, dist, colors);
        g.setPaint(rgp);
        g.fillOval(decimosCenterX - raioExterno, centroY - raioExterno, 2 * raioExterno, 2 * raioExterno);

        // Visor interno branco
        g.setColor(Color.WHITE);
        g.fillOval(decimosCenterX - raioInterno, centroY - raioInterno, 2 * raioInterno, 2 * raioInterno);

        // --- EFEITO INTERNO DO BISEL (SOBRE O VISOR BRANCO) ---
        // Sombra interna (sombra projetada no visor branco)
        g.setColor(new Color(0, 0, 0, 50));
        g.setStroke(new BasicStroke(3));
        g.drawArc(decimosCenterX - raioInterno, centroY - raioInterno, 2 * raioInterno, 2 * raioInterno, 225, 180);

        // Destaque interno (luz batendo na borda interna)
        g.setColor(new Color(255, 255, 255, 40));
        g.setStroke(new BasicStroke(2));
        g.drawArc(decimosCenterX - raioInterno, centroY - raioInterno, 2 * raioInterno, 2 * raioInterno, 45, 180);


        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(1));
        g.drawOval(decimosCenterX - raioInterno, centroY - raioInterno, 2 * raioInterno, 2 * raioInterno);

        // --- VISOR DIGITAL ---
        int visorWidth = 28, visorHeight = 38, visorY = centroY - 75, gap = 4;
        int totalDisplayWidth = 6 * visorWidth + 5 * gap;
        int startX = decimosCenterX - totalDisplayWidth / 2;
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
        int infoX = decimosCenterX - 145;
        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString("H-B", infoX, centroY - 8);
        g.drawString("V-A", infoX, centroY + 10);
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("Qn: 1,5 m³/h", infoX, centroY + 30);
        g.drawString("Qmin: 0,030 m³/h", infoX, centroY + 45);

        int seloY = centroY + 60;
        int seloDiametro = 38;

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(infoX, seloY, seloDiametro, seloDiametro);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("N", infoX + 11, seloY + 27);
        g.setStroke(new BasicStroke(2.0f));
        g.drawLine(infoX + 9, seloY + 29, infoX + 26, seloY + 11);

        // --- Elemento Central (indicador de fluxo) ---
        int circuloCentralY = centroY + 15;
        int circuloRaio = 16;
        g.setColor(Color.BLACK);
        g.fillArc(decimosCenterX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 0, 90);
        g.fillArc(decimosCenterX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 180, 90);
        g.setColor(Color.WHITE);
        g.fillArc(decimosCenterX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 90, 90);
        g.fillArc(decimosCenterX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 270, 90);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(decimosCenterX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2);

        // --- Relógios Analógicos ---
        int raioMostrador = 42;
        int litrosCenterX = decimosCenterX + 85;
        int litrosCenterY = centroY + 65;
        int decimosCenterY = centroY + 125;

        float litrosValor = (total_m3 * 1000f) % 10f;
        desenharMostrador(g, litrosCenterX, litrosCenterY, raioMostrador, litrosValor);
        float decimosValor = (total_m3 * 10000f) % 10f;
        desenharMostrador(g, decimosCenterX, decimosCenterY, raioMostrador, decimosValor);

        g.dispose();
        return imagem;
    }

    private void desenharMostrador(Graphics2D g, int centerX, int centerY, int radius, float value) {
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(2));
        g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
        g.setFont(new Font("Arial", Font.BOLD, 11));
        FontMetrics fmLabel = g.getFontMetrics();
        int labelWidth = fmLabel.stringWidth("");
        g.drawString("", centerX - labelWidth / 2, centerY - radius - 8);
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
