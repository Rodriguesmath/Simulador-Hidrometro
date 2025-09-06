import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Display {
    private BufferedImage gerarImagemAnalogica(float m3, float mm3, float pressao) {
        int width = 400, height = 250;
        BufferedImage imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagem.createGraphics();

        // Fundo branco
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Desenhar rolos para m³ (ex: 5 dígitos)
        int xRolo = 40, yRolo = 60, roloWidth = 40, roloHeight = 60;
        int m3int = (int) m3;
        String m3str = String.format("%05d", m3int);
        g.setFont(new Font("Monospaced", Font.BOLD, 36));
        for (int i = 0; i < m3str.length(); i++) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRoundRect(xRolo + i * (roloWidth + 5), yRolo, roloWidth, roloHeight, 10, 10);
            g.setColor(Color.BLACK);
            g.drawRoundRect(xRolo + i * (roloWidth + 5), yRolo, roloWidth, roloHeight, 10, 10);
            g.drawString(String.valueOf(m3str.charAt(i)), xRolo + 10 + i * (roloWidth + 5), yRolo + 45);
        }

        // Ponteiro para submúltiplos (litros e mililitros)
        int centroX = 300, centroY = 90, raio = 40;
        g.setColor(Color.BLACK);
        g.drawOval(centroX - raio, centroY - raio, 2 * raio, 2 * raio);
        // Ponteiro vermelho para litros (mm³ / 1_000_000)
        float litros = (mm3 % 1_000_000_000) / 1_000_000f;
        double anguloLitros = Math.toRadians((litros / 1000f) * 360.0 - 90);
        int ponteiroX = centroX + (int) (raio * Math.cos(anguloLitros));
        int ponteiroY = centroY + (int) (raio * Math.sin(anguloLitros));
        g.setColor(Color.RED);
        g.drawLine(centroX, centroY, ponteiroX, ponteiroY);

        // Texto da pressão
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString(String.format("Pressão: %.2f Pa", pressao), 40, 180);

        g.dispose();
        return imagem;
    }

    public void exibirImagem(float m3, float mm3, float pressao) {
        BufferedImage img = gerarImagemAnalogica(m3, mm3, pressao);
        JFrame frame = new JFrame("Hidrômetro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(img.getWidth(), img.getHeight());
        JLabel label = new JLabel(new ImageIcon(img));
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
    }
}