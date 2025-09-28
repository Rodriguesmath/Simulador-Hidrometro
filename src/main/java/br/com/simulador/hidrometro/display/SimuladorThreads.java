package main.java.br.com.simulador.hidrometro.display;

import main.java.br.com.simulador.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SimuladorThreads {
    private final String configPath;

    public SimuladorThreads(String configPath) {
        this.configPath = configPath;
    }

    public void startThreads() {
        int quantidade = askQuantidade();
        if (quantidade <= 0) {
            System.out.println("Execução cancelada. Quantidade inválida.");
            return;
        }

        final List<Thread> threads = new ArrayList<>();
        final CountDownLatch stopLatch = new CountDownLatch(1);

        for (int i = 1; i <= quantidade; i++) {
            final int indice = i;
            final String instanciaId = String.format("%02d", indice);
            Thread t = new Thread(() -> {
                try {
                    System.out.println("Iniciando simulação #" + indice);
                    Controller controller = new Controller(configPath, instanciaId);
                    controller.startSimulacao();
                    System.out.println("Simulação #" + indice + " finalizada.");
                } catch (Exception e) {
                    System.err.println("Erro na simulação #" + indice);
                    e.printStackTrace();
                }
            }, "SimulacaoThread-" + i);
            threads.add(t);
            t.start();
        }

        SwingUtilities.invokeLater(() -> createControlWindow(quantidade, threads, stopLatch));

        try {
            stopLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Finalizando todas as simulações...");
        for (Thread t : threads) {
            if (t.isAlive()) t.interrupt();
        }
        for (Thread t : threads) {
            try {
                t.join(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Todas as simulações foram finalizadas.");
    }

    private void createControlWindow(int quantidade, List<Thread> threads, CountDownLatch stopLatch) {
        JFrame controlFrame = new JFrame("Controle de Simulações");
        controlFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        controlFrame.setSize(360, 140);
        controlFrame.setLayout(new BorderLayout(8, 8));
        controlFrame.setLocationRelativeTo(null);

        JLabel label = new JLabel("Foram iniciadas " + quantidade + " simulações.");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        controlFrame.add(label, BorderLayout.CENTER);

        JPanel south = new JPanel();
        JButton stopBtn = new JButton("Parar todas");
        stopBtn.addActionListener(e -> {
            stopBtn.setEnabled(false);
            for (Thread th : threads) {
                if (th.isAlive()) th.interrupt();
            }
            controlFrame.dispose();
            stopLatch.countDown();
        });
        south.add(stopBtn);
        controlFrame.add(south, BorderLayout.SOUTH);

        controlFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopBtn.doClick();
            }
        });

        controlFrame.setVisible(true);
    }

    private int askQuantidade() {
        while (true) {
            String input = JOptionPane.showInputDialog(null,
                    "Quantos hidrômetros deseja iniciar? (1-5)",
                    "Escolha a Quantidade",
                    JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                return -1;
            }
            try {
                int val = Integer.parseInt(input.trim());
                if (val >= 1 && val <= 5) {
                    return val;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Por favor, insira um número entre 1 e 5.",
                            "Entrada Inválida",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Entrada inválida. Por favor, insira um número entre 1 e 5.",
                        "Erro de Formato",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
