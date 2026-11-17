import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public final class FabricaAws implements FabricaRecursosNuvem {

    private static final String PROVEDOR = "Aws";

    private static final String PREFIXO_MAQUINA = "i-";
    private static final String PREFIXO_ARMAZENAMENTO = "s3://";
    private static final String PREFIXO_FILA = "sqs-";

    private static final BigDecimal CUSTO_HORA_MAQUINA = new BigDecimal("0.10");
    private static final BigDecimal CUSTO_GB_MES = new BigDecimal("0.023");
    private static final BigDecimal CUSTO_FIXO_FILA_MES = new BigDecimal("0.40");

    private static final TabelaPrecos PRECOS =
            new TabelaPrecos(CUSTO_HORA_MAQUINA, CUSTO_GB_MES, CUSTO_FIXO_FILA_MES);

    private static final AtomicInteger SEQUENCIA_MAQUINA = new AtomicInteger();
    private static final AtomicInteger SEQUENCIA_ARMAZENAMENTO = new AtomicInteger();
    private static final AtomicInteger SEQUENCIA_FILA = new AtomicInteger();

    @Override
    public String getNomeProvedor() {
        return PROVEDOR;
    }

    @Override
    public TabelaPrecos getTabelaPrecos() {
        return PRECOS;
    }

    @Override
    public MaquinaVirtual criarMaquinaVirtual() {
        return new MaquinaVirtualAws(gerarIdentificador(PREFIXO_MAQUINA, SEQUENCIA_MAQUINA));
    }

    @Override
    public ArmazenamentoObjetos criarArmazenamento(int capacidadeGb) {
        return new ArmazenamentoS3(gerarIdentificador(PREFIXO_ARMAZENAMENTO, SEQUENCIA_ARMAZENAMENTO), capacidadeGb);
    }

    @Override
    public FilaMensagens criarFila() {
        return new FilaSqs(gerarIdentificador(PREFIXO_FILA, SEQUENCIA_FILA));
    }

    private static String gerarIdentificador(String prefixo, AtomicInteger sequencia) {
        return prefixo + String.format("%03d", sequencia.incrementAndGet());
    }

    private static final class MaquinaVirtualAws implements MaquinaVirtual {

        private final String identificador;

        private MaquinaVirtualAws(String identificador) {
            this.identificador = identificador;
        }

        @Override
        public String getIdentificador() {
            return identificador;
        }

        @Override
        public String getProvedor() {
            return PROVEDOR;
        }

        @Override
        public BigDecimal getCustoPorHora() {
            return CUSTO_HORA_MAQUINA;
        }

        @Override
        public BigDecimal getCustoMensal() {
            return CUSTO_HORA_MAQUINA.multiply(TabelaPrecos.HORAS_POR_MES);
        }
    }

    private static final class ArmazenamentoS3 implements ArmazenamentoObjetos {

        private final String identificador;
        private final int capacidadeGb;

        private ArmazenamentoS3(String identificador, int capacidadeGb) {
            this.identificador = identificador;
            this.capacidadeGb = capacidadeGb;
        }

        @Override
        public String getIdentificador() {
            return identificador;
        }

        @Override
        public String getProvedor() {
            return PROVEDOR;
        }

        @Override
        public int getCapacidadeGb() {
            return capacidadeGb;
        }

        @Override
        public BigDecimal getCustoPorGbMes() {
            return CUSTO_GB_MES;
        }

        @Override
        public BigDecimal getCustoMensal() {
            return CUSTO_GB_MES.multiply(BigDecimal.valueOf(capacidadeGb));
        }
    }

    private static final class FilaSqs implements FilaMensagens {

        private final String identificador;

        private FilaSqs(String identificador) {
            this.identificador = identificador;
        }

        @Override
        public String getIdentificador() {
            return identificador;
        }

        @Override
        public String getProvedor() {
            return PROVEDOR;
        }

        @Override
        public BigDecimal getCustoMensal() {
            return CUSTO_FIXO_FILA_MES;
        }
    }
}
