package main.java.br.com.simulador.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Armazena as configurações da simulação. Atua como um DTO/POJO (Plain Old Java Object).
 * Esta versão mantém o construtor antigo por compatibilidade, delegando a lógica
 * de carregamento para a classe ConfigLoader.
 */
public class SimulatorConfig {

    private Bitola bitola;
    private int tempoExecucao;
    private int intervaloAtualizacao;
    private int escalaDeTempo;
    private float pressaoMinima;
    private float pressaoMaxima;
    private boolean simularAr;
    private String matricula;
    private final Map<String, String> perfilDeConsumoProps = new HashMap<>();

    /**
     * Construtor padrão. Cria um objeto de configuração vazio.
     * Usado pelo ConfigLoader.
     */
    public SimulatorConfig() {}

    /**
     * CONSTRUTOR DE COMPATIBILIDADE.
     * Permite que o código antigo continue funcionando com `new SimulatorConfig(path)`.
     * Internamente, ele agora delega o trabalho para o ConfigLoader.
     */
    public SimulatorConfig(String caminhoArquivo) {
        // 1. Cria o loader.
        ConfigLoader loader = new ConfigLoader();
        // 2. Pede para o loader criar um objeto de config temporário totalmente preenchido.
        SimulatorConfig configCarregada = loader.carregarDeArquivo(caminhoArquivo);
        // 3. Copia os dados do objeto carregado para este objeto (`this`).
        this.copiarDados(configCarregada);
    }

    /**
     * Método auxiliar para copiar os dados de outro objeto SimulatorConfig.
     */
    private void copiarDados(SimulatorConfig outraConfig) {
        this.bitola = outraConfig.bitola;
        this.tempoExecucao = outraConfig.tempoExecucao;
        this.intervaloAtualizacao = outraConfig.intervaloAtualizacao;
        this.escalaDeTempo = outraConfig.escalaDeTempo;
        this.pressaoMinima = outraConfig.pressaoMinima;
        this.pressaoMaxima = outraConfig.pressaoMaxima;
        this.simularAr = outraConfig.simularAr;
        this.matricula = outraConfig.matricula;
        // Limpa o mapa atual e copia todos os dados do mapa do outro objeto.
        this.perfilDeConsumoProps.clear();
        this.perfilDeConsumoProps.putAll(outraConfig.perfilDeConsumoProps);
    }

    // --- GETTERS ( ---
    public String getMatricula() { return matricula; }
    public boolean isSimularAr() { return simularAr; }
    public Bitola getBitola() { return bitola; }
    public int getTempoExecucao() { return tempoExecucao; }
    public int getIntervaloAtualizacao() { return intervaloAtualizacao; }
    public int getEscalaDeTempo() { return escalaDeTempo; }
    public float getPressaoMinima() { return pressaoMinima; }
    public float getPressaoMaxima() { return pressaoMaxima; }
    public String getPerfilDeConsumoProperty(String key) { return perfilDeConsumoProps.get(key); }

    // --- SETTERS ---
    // Usados pelo ConfigLoader para preencher um objeto vazio.
    public void setBitola(Bitola bitola) { this.bitola = bitola; }
    public void setTempoExecucao(int tempoExecucao) { this.tempoExecucao = tempoExecucao; }
    public void setIntervaloAtualizacao(int intervaloAtualizacao) { this.intervaloAtualizacao = intervaloAtualizacao; }
    public void setEscalaDeTempo(int escalaDeTempo) { this.escalaDeTempo = escalaDeTempo; }
    public void setPressaoMinima(float pressaoMinima) { this.pressaoMinima = pressaoMinima; }
    public void setPressaoMaxima(float pressaoMaxima) { this.pressaoMaxima = pressaoMaxima; }
    public void setSimularAr(boolean simularAr) { this.simularAr = simularAr; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public void addPerfilDeConsumoProperty(String key, String value) { this.perfilDeConsumoProps.put(key, value); }
}