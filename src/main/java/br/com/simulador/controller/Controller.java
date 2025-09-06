import br.com.simulador.config.SimulatorConfig;


public class Controller {
    private SimulatorConfig config;

    public Controller(SimulatorConfig config) {
        this.config = config;
    }

    public void run() {
        System.out.println("=== Simulador de Hidrômetro ===");
        System.out.println("Bitola: " + config.getBitola() + " mm");
        System.out.println("Tempo de execução: " + config.getTempoExecucao() + " s");
        System.out.println("Intervalo de atualização: " + config.getIntervaloAtualizacao() + " s");
        System.out.println("--------------------------------");

        int tempoDecorrido = 0;
        while (tempoDecorrido < config.getTempoExecucao()) {
            // Simulação: gerar valor do hidrômetro (exemplo aleatório)
            double valorHidrometro = Math.random() * 100; // placeholder
            System.out.printf("t=%ds | leitura: %.2f\n", tempoDecorrido, valorHidrometro);

            // aguarda o intervalo
            try {
                Thread.sleep(config.getIntervaloAtualizacao() * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            tempoDecorrido += config.getIntervaloAtualizacao();
        }

        System.out.println("=== Simulação encerrada ===");
    }
}
