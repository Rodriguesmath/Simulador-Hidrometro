package main.java.br.com.simulador.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SimulatorConfig {

    // TODO: Adicionar atributos conforme ajustes para ficar de acordo com a
    // especificação
    private Bitola bitola; // Enum Bitola
    private int tempoExecucao; // em segundos
    private int intervaloAtualizacao; // em segundos

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

                // TODO: Adicionar chaves conforme ajustes para ficar de acordo com a
                // especificação
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
                    default:
                        System.out.println("Chave desconhecida ignorada: " + chave);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar configuração: " + caminhoArquivo, e);
        }
    }

    // TODO: Adicionar Getters conforme ajustes para ficar de acordo com a
    // especificação

    public Bitola getBitola() {
        return bitola;
    }

    public int getTempoExecucao() {
        return tempoExecucao;
    }

    public int getIntervaloAtualizacao() {
        return intervaloAtualizacao;
    }
}
