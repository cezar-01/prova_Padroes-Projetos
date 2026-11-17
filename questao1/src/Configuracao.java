import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public final class Configuracao {

    private static final String ARQUIVO = "config.properties";
    private static final String CHAVE_REGIAO = "regiao.padrao";
    private static final String CHAVE_ORCAMENTO = "orcamento.mensal.maximo";

    private static final String[] CAMINHOS_ALTERNATIVOS = {
            ARQUIVO, "src/" + ARQUIVO, "questao1/src/" + ARQUIVO
    };

    private static final AtomicInteger LEITURAS = new AtomicInteger();

    private final String regiaoPadrao;
    private final BigDecimal orcamentoMensalMaximo;

    private Configuracao() {
        Properties propriedades = carregar();
        this.regiaoPadrao = obrigatoria(propriedades, CHAVE_REGIAO);
        this.orcamentoMensalMaximo = new BigDecimal(obrigatoria(propriedades, CHAVE_ORCAMENTO));
        LEITURAS.incrementAndGet();
        System.out.println("[Configuracao] " + ARQUIVO + " lido pela thread "
                + Thread.currentThread().getName() + " (região padrão = " + regiaoPadrao
                + ", orçamento máximo = " + Moeda.formatar(orcamentoMensalMaximo) + ")");
    }

    private static final class Holder {
        private static final Configuracao INSTANCIA = new Configuracao();
    }

    public static Configuracao getInstancia() {
        return Holder.INSTANCIA;
    }

    public String getRegiaoPadrao() {
        return regiaoPadrao;
    }

    public BigDecimal getOrcamentoMensalMaximo() {
        return orcamentoMensalMaximo;
    }

    public static int getQuantidadeLeituras() {
        return LEITURAS.get();
    }

    private static Properties carregar() {
        Properties propriedades = new Properties();
        try (InputStream doClasspath = Configuracao.class.getClassLoader().getResourceAsStream(ARQUIVO)) {
            if (doClasspath != null) {
                try (Reader leitor = new InputStreamReader(doClasspath, StandardCharsets.UTF_8)) {
                    propriedades.load(leitor);
                    return propriedades;
                }
            }
            for (String caminho : CAMINHOS_ALTERNATIVOS) {
                Path arquivo = Paths.get(caminho);
                if (Files.isRegularFile(arquivo)) {
                    try (Reader leitor = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8)) {
                        propriedades.load(leitor);
                        return propriedades;
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao ler " + ARQUIVO, e);
        }
        throw new IllegalStateException("Arquivo " + ARQUIVO + " não encontrado no classpath nem no diretório "
                + Paths.get("").toAbsolutePath());
    }

    private static String obrigatoria(Properties propriedades, String chave) {
        String valor = propriedades.getProperty(chave);
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalStateException("Chave obrigatória ausente em " + ARQUIVO + ": " + chave);
        }
        return valor.trim();
    }
}
