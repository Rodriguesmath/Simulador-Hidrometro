package main.java.br.com.simulador.hidrometro.display;

import main.java.br.com.simulador.config.SimulatorConfig;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Serviço especializado na persistência (salvamento) de imagens em disco.
 *
 * Esta classe tem a **única responsabilidade** de lidar com operações de I/O (Entrada/Saída) de arquivos.
 * Uma de suas características mais importantes é que ela executa o salvamento em uma **thread separada (background)**.
 * Isso evita que a interface gráfica (UI) congele ou fique lenta durante o processo de escrita no disco,
 * que pode ser uma operação demorada.
 */
public class ImagePersistenceService {

    /** Armazena a configuração da simulação para acessar dados como a matrícula. */
    private final SimulatorConfig config;

    /**
     * Gerenciador da thread de background.
     * 'newSingleThreadExecutor' cria uma única thread que executa tarefas em sequência,
     * uma após a outra. Isso é ideal aqui para garantir que as imagens sejam salvas
     * na ordem em que foram solicitadas, sem concorrência.
     */
    private final ExecutorService saveExecutor = Executors.newSingleThreadExecutor();

    /**
     * Constrói o serviço de persistência de imagem.
     *
     * @param config A configuração da simulação, necessária para obter a matrícula do hidrômetro.
     */
    public ImagePersistenceService(SimulatorConfig config) {
        this.config = config;
    }

    /**
     * Agenda uma imagem para ser salva em disco.
     *
     * Este método **não** salva a imagem diretamente. Ele submete a tarefa de salvamento
     * para a thread de background e retorna imediatamente, mantendo a aplicação responsiva.
     *
     * @param imagem A imagem {@link BufferedImage} a ser salva.
     * @param m3Atual O valor atual de m³ medido, usado para nomear o arquivo.
     */
    public void salvarImagem(BufferedImage imagem, int m3Atual) {
        // 'submit' adiciona a tarefa (definida por uma expressão lambda) à fila do executor.
        // A tarefa será executada pela thread de background assim que possível.
        saveExecutor.submit(() -> performSave(imagem, m3Atual));
    }

    /**
     * Executa a lógica real de salvamento do arquivo.
     * **Este método é sempre executado na thread de background.**
     *
     * @param imagem A imagem a ser escrita no disco.
     * @param m3Atual O valor de m³ para o nome do arquivo.
     */
    private void performSave(BufferedImage imagem, int m3Atual) {
        // Pega a matrícula do hidrômetro a partir do objeto de configuração.
        String matricula = config.getMatricula();
        // Validação para garantir que a matrícula existe antes de prosseguir.
        if (matricula == null || matricula.trim().isEmpty()) {
            System.err.println("AVISO: Matrícula não configurada. Não foi possível salvar a imagem.");
            return; // Interrompe a operação se a matrícula não estiver disponível.
        }

        // --- Lógica de Criação do Diretório ---
        // Define o nome do diretório baseado na matrícula para organizar as medições.
        String nomeDiretorio = "Medicoes_" + matricula;
        File diretorio = new File(nomeDiretorio);
        // Verifica se o diretório já existe; se não, tenta criá-lo.
        if (!diretorio.exists()) {
            // mkdirs() cria também os diretórios pai, se necessário, sendo mais robusto.
            if (!diretorio.mkdirs()) {
                System.err.println("ERRO: Não foi possível criar o diretório: " + nomeDiretorio);
                return; // Interrompe se não conseguir criar o diretório.
            }
        }

        // --- Lógica de Nomenclatura do Arquivo ---
        // Usa o operador de módulo (%) para criar um ciclo de 0 a 99.
        // Isso significa que após 99 m³, as imagens começarão a ser sobrescritas (00.jpeg, 01.jpeg, ...).
        int numeroArquivo = m3Atual % 100;
        // Formata o nome do arquivo para ter sempre dois dígitos, com um zero à esquerda se necessário (ex: 5 -> "05.jpeg").
        String nomeArquivo = String.format("%02d.jpeg", numeroArquivo);
        // Cria o objeto File final, combinando o caminho do diretório e o nome do arquivo.
        File arquivoDeSaida = new File(diretorio, nomeArquivo);

        // --- Escrita do Arquivo em Disco ---
        try {
            // Usa a classe ImageIO do Java para escrever a imagem no formato JPEG.
            ImageIO.write(imagem, "jpeg", arquivoDeSaida);
            System.out.println("Imagem salva: " + arquivoDeSaida.getPath());
        } catch (IOException e) {
            // Captura possíveis erros de I/O (ex: disco cheio, falta de permissão) e informa no console.
            System.err.println("ERRO ao salvar a imagem " + nomeArquivo);
            e.printStackTrace();
        }
    }

    /**
     * Encerra o serviço de thread de forma graciosa.
     * Este método deve ser chamado ao final da aplicação para garantir que a thread de
     * background seja terminada corretamente.
     */
    public void shutdown() {
        // 'shutdown()' impede que novas tarefas sejam submetidas,
        // mas permite que as tarefas já na fila terminem de executar antes de parar a thread.
        saveExecutor.shutdown();
    }
}