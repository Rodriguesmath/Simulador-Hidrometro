package main.java.br.com.simulador.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Carrega e armazena todas as configurações da simulação a partir de um arquivo.
 */
public class SimulatorConfig {

    private Bitola bitola;
    private int tempoExecucao;
    private int intervaloAtualizacao;
    private int escalaDeTempo; // Novo parâmetro
    private float pressaoMinima;
    private float pressaoMaxima;
    private final Map<String, String> perfilDeConsumoProps = new HashMap<>();

    public SimulatorConfig(String caminhoArquivo) {
        carregarArquivo(caminhoArquivo);
    }

    private void carregarArquivo(String caminhoArquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("#"))
                    continue;

                String[] partes = linha.split("=");
                if (partes.length != 2)
                    continue;

                String chave = partes[0].trim();
                String valor = partes[1].trim();

                if (chave.startsWith("madrugada_") || chave.startsWith("manha_") || chave.startsWith("tarde_") || chave.startsWith("noite_")) {
                    perfilDeConsumoProps.put(chave, valor);
                    continue;
                }

                switch (chave) {
                    case "bitola":
                        this.bitola = Bitola.fromString(valor);
                        break;
                    case "tempoExecucao":
                        this.tempoExecucao = Integer.parseInt(valor);
                        break;
                    case "intervaloAtualizacao":
                        this.intervaloAtualizacao = Integer.parseInt(valor);
                        break;
                    case "escalaDeTempo":
                        this.escalaDeTempo = Integer.parseInt(valor);
                        break;
                    case "pressaoMinima":
                        this.pressaoMinima = Float.parseFloat(valor);
                        break;
                    case "pressaoMaxima":
                        this.pressaoMaxima = Float.parseFloat(valor);
                        break;
                    default:
                        System.out.println("Chave desconhecida ignorada: " + chave );
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar configuração: " + caminhoArquivo, e);
        }
    }

    // Getters para as configurações
    public Bitola getBitola() {
        return bitola;
    }

    public int getTempoExecucao() {
        return tempoExecucao;
    }

    public int getIntervaloAtualizacao() {
        return intervaloAtualizacao;
    }

    public int getEscalaDeTempo() {
        return escalaDeTempo;
    }

    public float getPressaoMinima() {
        return pressaoMinima;
    }

    public float getPressaoMaxima() {
        return pressaoMaxima;
    }

    public String getPerfilDeConsumoProperty(String key) {
        return perfilDeConsumoProps.get(key);
    }
}

