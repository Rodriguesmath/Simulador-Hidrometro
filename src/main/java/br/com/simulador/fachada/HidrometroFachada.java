package main.java.br.com.simulador.fachada;

import main.java.br.com.simulador.controller.Controller;
import main.java.br.com.simulador.hidrometro.ControleVazao;
import main.java.br.com.simulador.hidrometro.display.Display;
import main.java.br.com.simulador.hidrometro.Saida;
import main.java.br.com.simulador.hidrometro.display.ImagePersistenceService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HidrometroFachada {

    private static volatile HidrometroFachada instancia;

    private HidrometroFachada() {
        //Inicializa o mapa de instâncias ativas.
        this.instanciasAtivas = new ConcurrentHashMap<>();
        this.defaultConfigPath = "config/config.txt";
    }

    public static HidrometroFachada getInstancia() {
        if(instancia == null) {
            synchronized (HidrometroFachada.class) {
                if(instancia == null) {
                    instancia = new HidrometroFachada();
                }
            }
        }
        return instancia;
    }

    //Armazena as instâncias ativas de simulação, usando o ID da instância como chave.
    private final Map<String, SimulacaoAtiva> instanciasAtivas;
    private String defaultConfigPath;

    private record SimulacaoAtiva(Thread thread, Controller controller) {}

    //Define o caminho padrão do arquivo de configuração a ser usado nas novas instâncias.
    public void configSimuladorSHA(String configPath) {
        this.defaultConfigPath = configPath;
        System.out.println("[Fachada] Caminho de configuração padrão definido para: " + configPath);
    }

    /**
     * Cria e inicia uma nova instância de simulação do hidrômetro (SHA)
     * em uma thread separada. (Ex: hidro_01, hidro_02, etc.)
     *
     * @param instanciaId Um ID único para esta simulação (ex: "hidro_01")
     */
    public void criaSHA(String instanciaId) {
        if(instanciasAtivas.containsKey(instanciaId)) {
            System.out.println("[Fachada] Erro: A instância '" + instanciaId + "' já está em execução.");
            return;
        }

        Runnable simulacaoRunnable = () -> {
            Controller controller = null;
            try{
                controller = new Controller(defaultConfigPath, instanciaId);

                SimulacaoAtiva sa = new SimulacaoAtiva(Thread.currentThread(), controller);
                instanciasAtivas.put(instanciaId, sa);
                System.out.println("[Fachada] Instância '" + instanciaId + "' iniciada.");

                controller.startSimulacao();;
            } catch (Exception e) {
                if(e instanceof InterruptedException) {
                    System.out.println("[Fachada] Instância '" + instanciaId + "' interrompida.");
                } else {
                    System.out.println("[Fachada] Erro fatal na instância '" + instanciaId + "': " + e.getMessage());
                    e.printStackTrace();
                }
            } finally {
                instanciasAtivas.remove(instanciaId);
                System.out.println("[Fachada] Instância '" + instanciaId + "' finalizada.");
            }
        };

        Thread simulacaoThread = new Thread(simulacaoRunnable, "SimulacaoThread-" + instanciaId);
        simulacaoThread.start();
    }

    /**
     * Solicita a finalização de uma instância de simulação em execução.
     *
     * @param instanciaId O ID da simulação a ser finalizada.
     */
    public void finalizaSHA(String instanciaId) {
        SimulacaoAtiva sa = instanciasAtivas.get(instanciaId);
        if(sa != null) {
            sa.thread().interrupt();
        } else {
            System.out.println("[Fachada] Erro: Nenhuma instância ativa encontrada com o ID '" + instanciaId + "'.");
        }
    }

    /**
     * Altera a vazão de uma instância de simulação em tempo real.
     *
     * @param instanciaId O ID da simulação a ser modificada.
     * @param percentual O novo valor da vazão (0 a 100).
     */
    public void modificaVazaoSHA(String instanciaId, int percentual) {
        SimulacaoAtiva sa = instanciasAtivas.get(instanciaId);
        if(sa != null) {
            try {
                ControleVazao cv = sa.controller().getControleVazao();
                cv.setMultiplicador(percentual);
                System.out.println("[Fachada] Vazão da instância '" + instanciaId + "' ajustada para " + percentual + "%.");
            } catch(Exception e) {
                System.out.println("[Fachada] Erro ao modificar vazão da instância '" + instanciaId + "': " + e.getMessage());
            }
        } else {
            System.out.println("[Fachada] Erro: Nenhuma instância ativa encontrada com o ID '" + instanciaId + "'.");
        }
    }

    /**
     * Habilita ou desabilita a geração de imagens JPEG para uma instância de simulação.
     *
     * @param instanciaId O ID da simulação.
     * @param habilitar   'true' para habilitar, 'false' para desabilitar.
     */
    public void habilitaGeracaoImagemSHA(String instanciaId, boolean habilitar) {
        SimulacaoAtiva sa = instanciasAtivas.get(instanciaId);
        if(sa != null) {
            try {
                Display display = sa.controller().getDisplay();
                ImagePersistenceService ips = display.getImageSaver();

                ips.setHabilitado(habilitar);
                String status = habilitar ? "HABILITADA" : "DESABILITADA";
                System.out.println("[Fachada] Geração de imagens da instância '" + instancia);
            } catch (Exception e) {
                System.out.println("[Fachada] Erro ao modificar geração de imagens da instância '" + instanciaId + "': " + e.getMessage());
            }
        } else {
            System.out.println("[Fachada] Erro: Nenhuma instância ativa encontrada com o ID '" + instanciaId + "'.");
        }
    }

    /**
     * Habilita ou desabilita a saída de log no console para uma simulação.
     *
     * @param instanciaId O ID da simulação.
     * @param habilitar "true" para habilitar, "false" para desabilitar.
     */
    public void habilitaLogConsoleSHA(String instanciaId, boolean habilitar) {
        SimulacaoAtiva sa = instanciasAtivas.get(instanciaId);
        if(sa != null) {
            try {
                Saida saida = sa.controller().getSaida();
                saida.setHabilitado(habilitar);
                String status = habilitar ? "HABILITADO" : "DESABILITADO";
                System.out.println("[Fachada] Log no console da instância '" + instanciaId);
            } catch( Exception e) {
                System.out.println("[Fachada] Erro ao modificar log no console da instância '" + instanciaId + "': " + e.getMessage());
            }
        } else {
            System.out.println("[Fachada] Erro: Nenhuma instância ativa encontrada com o ID '" + instanciaId + "'.");
        }
    }

    /**
     * Habilita ou desabilita o log de salvamento de imagem no console.
     *
     * @param instanciaId O ID da simulação.
     * @param habilitar "true" para habilitar, "false" para desabilitar.
     */
    public void habilitaLogImagemSHA(String instanciaId, boolean habilitar) {
        SimulacaoAtiva sa = instanciasAtivas.get(instanciaId);
        if(sa != null) {
            try {
                ImagePersistenceService ips = sa.controller().getDisplay().getImageSaver();

                ips.setLogHabilitado(habilitar);
                String status = habilitar ? "HABILITADO" : "DESABILITADO";
                System.out.println("[Fachada] Log de imagem da instância '" + instanciaId + "' " + status + ".");
            } catch (Exception e) {
                System.out.println("[Fachada] Erro ao modificar log de imagem da instância '" + instanciaId + "': " + e.getMessage());
            }
        } else {
            System.out.println("[Fachada] Erro: Nenhuma instância ativa encontrada com o ID '" + instanciaId + "'.");
        }
    }
}
