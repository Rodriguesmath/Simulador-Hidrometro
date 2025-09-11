package main.java.br.com.simulador.hidrometro;

import main.java.br.com.simulador.config.SimulatorConfig;
import main.java.br.com.simulador.observer.Observador;
import main.java.br.com.simulador.strategy.PerfilDeConsumoStrategy;
import main.java.br.com.simulador.strategy.PerfilMadrugada;
import main.java.br.com.simulador.strategy.PerfilManha;
import main.java.br.com.simulador.strategy.PerfilNoite;
import main.java.br.com.simulador.strategy.PerfilTarde;

import java.util.ArrayList;
import java.util.List;

/**
 * Orquestra a simulação do hidrômetro.
 * Atua como o "Subject" no padrão Observer, notificando os observadores.
 */
public class HidrometroSimulator {

    private final Medidor medidor;
    private final SimulatorConfig config;
    private final ControleVazao controleVazao;
    private final List<Observador> observadores = new ArrayList<>();
    private final List<PerfilDeConsumoStrategy> estrategias = new ArrayList<>();
    private int tempoTotalSimulado = 0;

    public HidrometroSimulator(SimulatorConfig config, ControleVazao controleVazao) {
        this.config = config;
        this.controleVazao = controleVazao;

        // Inicializa as estratégias de consumo com suas configurações
        inicializarEstrategias();

        // Cria uma "Entrada" inicial para definir o estado do medidor em t=0
        Entrada entradaInicial = new Entrada(0, config, this.estrategias, this.controleVazao);
        this.medidor = new Medidor(entradaInicial);
    }

    /**
     * Lê a configuração e instancia cada estratégia com seus respectivos parâmetros.
     */
    private void inicializarEstrategias() {
        // Madrugada
        float madMinVel = Float.parseFloat(config.getPerfilDeConsumoProperty("madrugada_vel_min"));
        float madMaxVel = Float.parseFloat(config.getPerfilDeConsumoProperty("madrugada_vel_max"));
        int madInicio = Integer.parseInt(config.getPerfilDeConsumoProperty("madrugada_inicio"));
        int madFim = Integer.parseInt(config.getPerfilDeConsumoProperty("madrugada_fim"));
        estrategias.add(new PerfilMadrugada(madMinVel, madMaxVel, madInicio, madFim));

        // Manhã
        float manhaMinVel = Float.parseFloat(config.getPerfilDeConsumoProperty("manha_vel_min"));
        float manhaMaxVel = Float.parseFloat(config.getPerfilDeConsumoProperty("manha_vel_max"));
        int manhaInicio = Integer.parseInt(config.getPerfilDeConsumoProperty("manha_inicio"));
        int manhaFim = Integer.parseInt(config.getPerfilDeConsumoProperty("manha_fim"));
        estrategias.add(new PerfilManha(manhaMinVel, manhaMaxVel, manhaInicio, manhaFim));

        // Tarde
        float tardeMinVel = Float.parseFloat(config.getPerfilDeConsumoProperty("tarde_vel_min"));
        float tardeMaxVel = Float.parseFloat(config.getPerfilDeConsumoProperty("tarde_vel_max"));
        int tardeInicio = Integer.parseInt(config.getPerfilDeConsumoProperty("tarde_inicio"));
        int tardeFim = Integer.parseInt(config.getPerfilDeConsumoProperty("tarde_fim"));
        estrategias.add(new PerfilTarde(tardeMinVel, tardeMaxVel, tardeInicio, tardeFim));

        // Noite
        float noiteMinVel = Float.parseFloat(config.getPerfilDeConsumoProperty("noite_vel_min"));
        float noiteMaxVel = Float.parseFloat(config.getPerfilDeConsumoProperty("noite_vel_max"));
        int noiteInicio = Integer.parseInt(config.getPerfilDeConsumoProperty("noite_inicio"));
        int noiteFim = Integer.parseInt(config.getPerfilDeConsumoProperty("noite_fim"));
        estrategias.add(new PerfilNoite(noiteMinVel, noiteMaxVel, noiteInicio, noiteFim));
    }


    public void adicionarObservador(Observador obs) {
        this.observadores.add(obs);
    }

    /**
     * Notifica os observadores sobre o estado atual.
     * Deve ser chamado uma vez no início para mostrar o estado t=0.
     */
    public void notificarObservadores() {
        for (Observador obs : observadores) {
            obs.atualizar(medidor, tempoTotalSimulado);
        }
    }

    /**
     * Notifica todos os observadores que a simulação terminou.
     */
    public void notificarFimSimulacao() {
        for (Observador obs : observadores) {
            obs.simulacaoFinalizada(medidor);
        }
    }

    /**
     * Avança a simulação em um passo de tempo.
     */
    public void avancarSimulacao() {
        int tempoParaAvancar = config.getEscalaDeTempo();
        tempoTotalSimulado += tempoParaAvancar;

        Entrada novaEntrada = new Entrada(tempoTotalSimulado, config, estrategias, this.controleVazao);
        medidor.atualizarMedicao(novaEntrada, tempoParaAvancar);

        notificarObservadores();
    }

    public boolean isFinalizado() {
        int tempoExecucaoConfigurado = config.getTempoExecucao();
        if (tempoExecucaoConfigurado == -1) {
            return false;
        }
        return tempoTotalSimulado >= tempoExecucaoConfigurado;
    }

    public Medidor getMedidor() {
        return medidor;
    }
}

