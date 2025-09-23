package main.java.br.com.simulador.hidrometro.display;

import main.java.br.com.simulador.config.SimulatorConfig;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Responsabilidade Única: Salvar imagens do hidrômetro em disco.
 * Gerencia sua própria thread para não bloquear a UI.
 */
public class ImagePersistenceService {

    private final SimulatorConfig config;
    private final ExecutorService saveExecutor = Executors.newSingleThreadExecutor();

    public ImagePersistenceService(SimulatorConfig config) {
        this.config = config;
    }

    public void salvarImagem(BufferedImage imagem, int m3Atual) {
        saveExecutor.submit(() -> performSave(imagem, m3Atual));
    }

    private void performSave(BufferedImage imagem, int m3Atual) {
        String matricula = config.getMatricula();
        if (matricula == null || matricula.trim().isEmpty()) {
            System.err.println("AVISO: Matrícula não configurada. Não foi possível salvar a imagem.");
            return;
        }
        String nomeDiretorio = "Medicoes_" + matricula;
        File diretorio = new File(nomeDiretorio);
        if (!diretorio.exists()) {
            if (!diretorio.mkdirs()) {
                System.err.println("ERRO: Não foi possível criar o diretório: " + nomeDiretorio);
                return;
            }
        }
        int numeroArquivo = m3Atual % 100;
        String nomeArquivo = String.format("%02d.jpeg", numeroArquivo);
        File arquivoDeSaida = new File(diretorio, nomeArquivo);
        try {
            ImageIO.write(imagem, "jpeg", arquivoDeSaida);
            System.out.println("Imagem salva: " + arquivoDeSaida.getPath());
        } catch (IOException e) {
            System.err.println("ERRO ao salvar a imagem " + nomeArquivo);
            e.printStackTrace();
        }
    }

    public void shutdown() {
        saveExecutor.shutdown();
    }
}
