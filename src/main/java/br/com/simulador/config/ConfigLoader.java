package main.java.br.com.simulador.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Especialista no carregamento de configurações a partir de um arquivo.
 *
 * Esta classe aplica o **Princípio da Responsabilidade Única (SRP)** de forma exemplar.
 * Sua única responsabilidade é ler um arquivo de configuração, analisar ("parse")
 * seu conteúdo e construir um objeto {@link SimulatorConfig} válido.
 *
 * Ela atua como uma **Factory** para objetos `SimulatorConfig`, escondendo
 * toda a complexidade de I/O (Entrada/Saída) e manipulação de Strings do resto da aplicação.
 */
public class ConfigLoader {

    /**
     * Lê um arquivo de configuração e retorna um objeto {@link SimulatorConfig} preenchido.
     * Este é o principal método de serviço da classe.
     *
     * @param caminhoArquivo O caminho para o arquivo de configuração (ex: "config/simulacao.properties").
     * @return Uma instância de {@link SimulatorConfig} com todos os dados carregados do arquivo.
     * @throws RuntimeException Se ocorrer um erro de I/O (ex: arquivo não encontrado),
     * encapsulando a {@link IOException} original.
     */
    public SimulatorConfig carregarDeArquivo(String caminhoArquivo) {
        // 1. Cria uma instância vazia de SimulatorConfig, que atuará como um "contêiner" para os dados.
        SimulatorConfig config = new SimulatorConfig();

        // 2. Utiliza a estrutura 'try-with-resources' para garantir que o leitor de arquivo (BufferedReader)
        // seja fechado automaticamente no final, evitando vazamento de recursos.
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            // 3. Itera sobre cada linha do arquivo até que o final seja alcançado (readLine() retorna null).
            while ((linha = br.readLine()) != null) {
                // Remove espaços em branco no início e no fim da linha para tornar o parse mais robusto.
                linha = linha.trim();

                // Ignora a linha se ela estiver vazia ou se for um comentário (iniciada com '#').
                if (linha.isEmpty() || linha.startsWith("#"))
                    continue; // O 'continue' pula para a próxima iteração do loop 'while'.

                // Divide a linha no caractere '=', que separa a chave do valor.
                String[] partes = linha.split("=");
                // Se a linha não contiver exatamente um '=', ela é considerada malformada e ignorada.
                if (partes.length != 2)
                    continue;

                // Extrai e limpa a chave e o valor.
                String chave = partes[0].trim();
                String valor = partes[1].trim();

                // 4. Preenche o objeto 'config' usando seus métodos setters.
                // Trata de forma especial as chaves relacionadas ao perfil de consumo,
                // adicionando-as a um mapa para maior flexibilidade.
                if (chave.startsWith("madrugada_") || chave.startsWith("manha_") || chave.startsWith("tarde_") || chave.startsWith("noite_")) {
                    config.addPerfilDeConsumoProperty(chave, valor);
                    continue; // Pula para a próxima linha após processar a propriedade de perfil.
                }

                // Utiliza um 'switch' para as chaves de configuração fixas.
                // Para cada chave, o valor (que é uma String) é convertido para o tipo de dado apropriado.
                switch (chave) {
                    case "bitola":
                        config.setBitola(Bitola.fromString(valor));
                        break;
                    case "tempoExecucao":
                        config.setTempoExecucao(Integer.parseInt(valor));
                        break;
                    case "intervaloAtualizacao":
                        config.setIntervaloAtualizacao(Integer.parseInt(valor));
                        break;
                    case "escalaDeTempo":
                        config.setEscalaDeTempo(Integer.parseInt(valor));
                        break;
                    case "pressaoMinima":
                        config.setPressaoMinima(Float.parseFloat(valor));
                        break;
                    case "pressaoMaxima":
                        config.setPressaoMaxima(Float.parseFloat(valor));
                        break;
                    case "simularAr":
                        config.setSimularAr(Boolean.parseBoolean(valor));
                        break;
                    case "matricula":
                        config.setMatricula(valor);
                        break;
                    default:
                        // Se uma chave no arquivo não for reconhecida, ela é ignorada, e um aviso é impresso.
                        // Isso torna o sistema mais robusto a configurações desconhecidas.
                        System.out.println("Chave desconhecida ignorada: " + chave);
                }
            }
        } catch (IOException e) {
            // Se um erro de I/O ocorrer (ex: arquivo não encontrado, sem permissão de leitura),
            // a aplicação não pode continuar de forma confiável.
            // A estratégia aqui é "falhar rápido" (fail-fast), lançando uma RuntimeException
            // que encerra a aplicação com uma mensagem de erro clara.
            throw new RuntimeException("Erro ao carregar configuração: " + caminhoArquivo, e);
        }

        // 5. Retorna o objeto de configuração totalmente preenchido e pronto para ser usado.
        return config;
    }
}