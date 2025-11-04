package main.java.br.com.simulador;

import main.java.br.com.simulador.fachada.HidrometroFachada;

import java.util.Scanner;

/**
 * Classe substituta da "Main" original e se comunica
 * EXCLUSIVAMENTE com a "HidrometroFachada".
 */
public class ClienteCLI {
    public static void main(String[] args) {
        HidrometroFachada fachada = HidrometroFachada.getInstancia();

        Scanner sc = new Scanner(System.in);
        boolean executando = true;

        exibirMenu();

        while(executando) {
            System.out.println("\nComando > ");
            String linha = sc.nextLine().trim();
            if(linha.isEmpty()) continue;

            String[] partes = linha.split(" ");
            String comando = partes[0].toLowerCase();

            try {
                switch(comando) {
                    case "config":
                        fachada.configSimuladorSHA(partes[1]);
                        break;
                    case "criar":
                        fachada.criaSHA(partes[1]);
                        break;
                    case "parar":
                        fachada.finalizaSHA(partes[1]);
                        break;
                    case "vazao":
                        fachada.modificaVazaoSHA(partes[1], Integer.parseInt(partes[2]));
                        break;
                    case "imagem":
                        fachada.habilitaGeracaoImagemSHA(partes[1], Boolean.parseBoolean(partes[2]));
                        break;
                    case "log":
                        fachada.habilitaLogConsoleSHA(partes[1], Boolean.parseBoolean(partes[2]));
                        break;
                    case "logimg":
                        fachada.habilitaLogImagemSHA(partes[1], Boolean.parseBoolean(partes[2]));
                        break;
                    case "menu":
                        exibirMenu();
                        break;
                    case "sair":
                        executando = false;
                        System.out.println("Encerrando o cliente CLI.");
                        break;
                    default:
                        System.out.println("Comando desconhecido: " + comando);
                }
            } catch (Exception e) {
                System.out.println("Erro ao processar comando '" + comando + "'. Verfique os argumentos e tente novamente.");
                System.out.println("Detalhe: " + e.getMessage());
            }
        }

        System.exit(0);
    }

    private static void exibirMenu() {
        System.out.println("========================================================================================");
        System.out.println("----------------- Cliente CLI do SHA (Simulador de Hidrômetro Analógico ---------------");
        System.out.println("========================================================================================");
        System.out.println("Use a Fachada para controlar as simulações:");
        System.out.println("  config [caminho]   - Define o arquivo de config. (Ex: config config/config.txt)");
        System.out.println("  criar [id]         - Cria e inicia uma simulação. (Ex: criar hidro_01)");
        System.out.println("  parar [id]         - Para uma simulação. (Ex: parar hidro_01)");
        System.out.println("  vazao [id] [0-100] - Modifica a vazão da simulação. (Ex: vazao hidro_01 50)");
        System.out.println("  imagem [id] [t/f]  - Habilita/desabilita salvar imagens. (Ex: imagem hidro_01 false)");
        System.out.println("  log [id] [t/f]     - Habilita/desabilita log no console. (Ex: log hidro_01 true)");
        System.out.println("  logimg [id] [t/f]  - Habilita/desabilita log de imagem. (Ex: logimg hidro_01 true)");
        System.out.println("  menu               - Exibe este menu.");
        System.out.println("  sair               - Fecha esta interface.");
        System.out.println("========================================================================================");
    }
}
