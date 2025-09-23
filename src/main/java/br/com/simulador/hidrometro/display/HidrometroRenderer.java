package main.java.br.com.simulador.hidrometro.display;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Especialista em renderização gráfica do hidrômetro.
 *
 * Esta classe tem a **única responsabilidade** de converter um valor numérico de consumo (m³)
 * em uma representação visual (`BufferedImage`). Ela é completamente **stateless** (sem estado),
 * o que significa que para a mesma entrada, ela sempre produzirá a mesma saída.
 * Isso a torna previsível, reutilizável e fácil de testar isoladamente.
 *
 * Utiliza a API Java 2D Graphics (`Graphics2D`) para realizar todo o desenho.
 */
public class HidrometroRenderer {

    /**
     * Gera uma imagem completa do hidrômetro analógico com base no consumo total.
     *
     * @param total_m3 O valor total de metros cúbicos consumidos a ser exibido.
     * @return um objeto {@link BufferedImage} contendo a imagem renderizada do hidrômetro.
     */
    public BufferedImage gerarImagemAnalogica(float total_m3) {
        // --- 1. Configuração Inicial do "Canvas" ---
        int width = 800, height = 500;
        // Cria a imagem em memória onde o desenho será feito.
        BufferedImage imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Obtém o "pincel" (contexto gráfico) para desenhar na imagem.
        Graphics2D g = (Graphics2D) imagem.getGraphics();

        // Ativa o anti-aliasing para suavizar as bordas de formas e textos, resultando em maior qualidade visual.
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Define um fundo branco inicial para a imagem.
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // --- 2. Definição de Pontos e Medidas Chave ---
        // Variáveis usadas como referência para posicionar os elementos de forma relativa.
        int decimosCenterX = width / 2;
        int centroY = height / 2;

        int raioExterno = 200;
        int raioInterno = 175;

        // --- 3. Desenho do Corpo Azul do Hidrômetro ---
        int corpoAlturaCentro = 300;
        int corpoAlturaBorda = 260;
        int conexaoAltura = 150;

        // GeneralPath é usado para criar uma forma customizada complexa.
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

        // Usa um gradiente de azul para simular um efeito 3D cilíndrico.
        GradientPaint gpBody = new GradientPaint(0, centroY - corpoAlturaCentro / 2.0f, new Color(65, 110, 180),
                0, centroY + corpoAlturaCentro / 2.0f, new Color(15, 40, 90));
        g.setPaint(gpBody);
        g.fill(body);

        // --- 4. Desenho da Carcaça Preta e Efeitos de Relevo (Bisel) ---
        // Desenha a base preta circular.
        Color basePreto = new Color(40, 40, 40);
        g.setColor(basePreto);
        g.fillOval(decimosCenterX - raioExterno, centroY - raioExterno, 2 * raioExterno, 2 * raioExterno);

        // Simula um efeito de bisel (bevel) desenhando arcos claros (luz) e escuros (sombra) nas bordas.
        // Destaque superior do bisel EXTERNO (simula a luz batendo de cima).
        g.setColor(new Color(70, 70, 70));
        g.setStroke(new BasicStroke(2));
        g.drawArc(decimosCenterX - raioExterno + 2, centroY - raioExterno + 2, 2 * raioExterno - 4, 2 * raioExterno - 4, 45, 180);

        // Sombra inferior do bisel EXTERNO (simula a sombra na parte de baixo).
        g.setColor(new Color(0, 0, 0, 100));
        g.drawArc(decimosCenterX - raioExterno + 2, centroY - raioExterno + 2, 2 * raioExterno - 4, 2 * raioExterno - 4, 225, 180);

        // Adiciona um gradiente radial para simular um reflexo de luz sutil no "vidro".
        Point2D center = new Point2D.Float(decimosCenterX - 60, centroY - 60);
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(255, 255, 255, 40), new Color(255, 255, 255, 0)};
        RadialGradientPaint rgp = new RadialGradientPaint(center, (float) raioExterno, dist, colors);
        g.setPaint(rgp);
        g.fillOval(decimosCenterX - raioExterno, centroY - raioExterno, 2 * raioExterno, 2 * raioExterno);

        // --- 5. Desenho do Visor Interno Branco ---
        g.setColor(Color.WHITE);
        g.fillOval(decimosCenterX - raioInterno, centroY - raioInterno, 2 * raioInterno, 2 * raioInterno);

        // Adiciona uma sombra interna projetada pela carcaça preta para dar profundidade.
        // Sombra interna (sombra projetada no visor branco)
        g.setColor(new Color(0, 0, 0, 50));
        g.setStroke(new BasicStroke(3));
        g.drawArc(decimosCenterX - raioInterno, centroY - raioInterno, 2 * raioInterno, 2 * raioInterno, 225, 180);

        // Destaque interno (luz batendo na borda interna)
        g.setColor(new Color(255, 255, 255, 40));
        g.setStroke(new BasicStroke(2));
        g.drawArc(decimosCenterX - raioInterno, centroY - raioInterno, 2 * raioInterno, 2 * raioInterno, 45, 180);

        // Desenha a linha de contorno final do visor.
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(1));
        g.drawOval(decimosCenterX - raioInterno, centroY - raioInterno, 2 * raioInterno, 2 * raioInterno);

        // --- 6. Desenho do Display Digital dos M³ ---
        int visorWidth = 28, visorHeight = 38, visorY = centroY - 75, gap = 4;
        int totalDisplayWidth = 6 * visorWidth + 5 * gap;
        int startX = decimosCenterX - totalDisplayWidth / 2;
        Font fontDigital = new Font("Arial", Font.BOLD, 30);
        g.setFont(fontDigital);

        // Desenha a base cinza do display digital com cantos arredondados.
        g.setColor(new Color(220, 220, 200));
        g.fill(new RoundRectangle2D.Float(startX - 3, visorY - 3, totalDisplayWidth + 6, visorHeight + 6, 8, 8));
        g.setColor(new Color(180, 180, 180));
        g.draw(new RoundRectangle2D.Float(startX - 3, visorY - 3, totalDisplayWidth + 6, visorHeight + 6, 8, 8));

        // A lógica matemática a seguir quebra o valor total de m³ em dígitos individuais.
        // Pega os 4 dígitos inteiros da medição. O módulo 10000 garante que o valor "zere" após 9999.
        int m3Inteiro = (int) total_m3;
        m3Inteiro = m3Inteiro % 10000;
        String m3Str = String.format("%04d", m3Inteiro); // Formata para ter sempre 4 dígitos (ex: 12 -> "0012").
        for (int i = 0; i < 4; i++) {
            int visorX = startX + i * (visorWidth + gap);
            g.setColor(Color.WHITE);
            g.fillRect(visorX, visorY, visorWidth, visorHeight);
            g.setColor(Color.BLACK);
            // FontMetrics é usado para medir o tamanho do texto e centralizá-lo perfeitamente na caixa.
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(String.valueOf(m3Str.charAt(i)));
            g.drawString(String.valueOf(m3Str.charAt(i)), visorX + (visorWidth - textWidth) / 2, visorY + 29);
        }
        // Pega os dígitos decimais que representarão as centenas e dezenas de litros.
        int centenasLitros = (int) ((total_m3 * 10) % 10); // 1º dígito decimal
        int dezenasLitros = (int) ((total_m3 * 100) % 10);  // 2º dígito decimal
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

        // Desenha as linhas divisórias entre os dígitos.
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(0.5f));
        for (int i = 1; i < 6; i++) {
            int lineX = startX + i * visorWidth + (i - 1) * gap + gap / 2;
            g.drawLine(lineX, visorY, lineX, visorY + visorHeight);
        }

        // Desenha a unidade de medida "m³".
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.BLACK);
        g.drawString("m³", startX + totalDisplayWidth + 8, visorY + 28);

        // --- 7. Desenho dos Textos Informativos e Selo ---
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

        // --- 8. Desenho do Indicador Central de Fluxo ---
        int circuloCentralY = centroY + 15;
        int circuloRaio = 16;
        // O efeito de "catavento" é criado desenhando quatro arcos de 90 graus, alternando as cores.
        g.setColor(Color.BLACK);
        g.fillArc(decimosCenterX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 0, 90);
        g.fillArc(decimosCenterX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 180, 90);
        g.setColor(Color.WHITE);
        g.fillArc(decimosCenterX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 90, 90);
        g.fillArc(decimosCenterX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2, 270, 90);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(decimosCenterX - circuloRaio, circuloCentralY - circuloRaio, circuloRaio * 2, circuloRaio * 2);

        // --- 9. Desenho dos Relógios Analógicos (Mostradores) ---
        int raioMostrador = 42;
        int litrosCenterX = decimosCenterX + 85;
        int litrosCenterY = centroY + 65;
        int decimosCenterY = centroY + 125;

        // Delega a tarefa repetitiva de desenhar um mostrador para um método auxiliar.
        float litrosValor = (total_m3 * 1000f) % 10f;      // Pega o valor dos litros (3º dígito decimal).
        desenharMostrador(g, litrosCenterX, litrosCenterY, raioMostrador, litrosValor);
        float decimosValor = (total_m3 * 10000f) % 10f; // Pega o valor dos décimos de litro (4º dígito decimal).
        desenharMostrador(g, decimosCenterX, decimosCenterY, raioMostrador, decimosValor);

        // --- 10. Limpeza de Recursos ---
        // Libera os recursos do sistema alocados pelo objeto Graphics. É uma boa prática fundamental.
        g.dispose();

        return imagem;
    }

    /**
     * Desenha um único mostrador analógico com números de 0 a 9 e um ponteiro.
     * Este é um método auxiliar para evitar a repetição de código.
     *
     * @param g      O contexto gráfico para desenhar.
     * @param centerX A coordenada X do centro do mostrador.
     * @param centerY A coordenada Y do centro do mostrador.
     * @param radius O raio do mostrador.
     * @param value  O valor (de 0.0 a 9.9...) que o ponteiro deve indicar.
     */
    private void desenharMostrador(Graphics2D g, int centerX, int centerY, int radius, float value) {
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(2));
        g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
        g.setFont(new Font("Arial", Font.BOLD, 11));
        FontMetrics fmLabel = g.getFontMetrics();
        int labelWidth = fmLabel.stringWidth("");
        g.drawString("", centerX - labelWidth / 2, centerY - radius - 8);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        // Posiciona os números de 0 a 9 em um círculo usando trigonometria.
        // 36 graus = 360 (círculo completo) / 10 (números). O -90 é para o '0' começar no topo.
        for (int i = 0; i < 10; i++) {
            double angle = Math.toRadians(i * 36 - 90);
            int numX = centerX + (int) ((radius - 11) * Math.cos(angle));
            int numY = centerY + (int) ((radius - 11) * Math.sin(angle));
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(String.valueOf(i));
            g.drawString(String.valueOf(i), numX - textWidth / 2, numY + 5);
        }

        // Calcula o ângulo do ponteiro com base no valor de entrada, usando a mesma lógica.
        double angle = Math.toRadians((value * 36) - 90);
        int pointerEndX = centerX + (int) ((radius - 8) * Math.cos(angle));
        int pointerEndY = centerY + (int) ((radius - 8) * Math.sin(angle));

        // Desenha uma sombra sutil para o ponteiro antes de desenhar o ponteiro real para dar profundidade.
        g.setColor(new Color(0,0,0,70));
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(centerX + 1, centerY + 1, pointerEndX + 1, pointerEndY + 1);

        // Desenha o ponteiro vermelho e o pino central.
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2)); // Garante que o ponteiro tenha a espessura correta.
        g.drawLine(centerX, centerY, pointerEndX, pointerEndY);
        g.fillOval(centerX - 3, centerY - 3, 6, 6);
    }
}