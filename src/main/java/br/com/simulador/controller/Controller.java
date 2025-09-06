import main.java.br.com.simulador.config.SimulatorConfig;
import main.java.br.com.simulador.hidrometro.HidrometroSimulator;


public class Controller {
    private SimulatorConfig config;
    private HidrometroSimulator simulator;

    public Controller(SimulatorConfig config) {
        this.config = config;
    }

    public void run() {
        simulator = new HidrometroSimulator(config.getBitola(), config.getIntervalo(), config.getQuantidade());
    }
}
