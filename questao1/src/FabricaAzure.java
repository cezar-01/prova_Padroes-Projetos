import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public final class FabricaAzure implements FabricaRecursosNuvem {

    private static final String PROVEDOR = "Azure";

    private static final String PREFIXO_MAQUINA = "vm-";
    private static final String PREFIXO_ARMAZENAMENTO = "blob://";
    private static final String PREFIXO_FILA = "sb-";

    private static final BigDecimal CUSTO_HORA_MAQUINA = new BigDecimal("0.12");
    private static final BigDecimal CUSTO_GB_MES = new BigDecimal("0.02");
    private static final BigDecimal CUSTO_FIXO_FILA_MES = new BigDecimal("0.50");

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
        return new MaquinaVirtualAzure(gerarIdentificador(PREFIXO_MAQUINA, SEQUENCIA_MAQUINA));
    }

    @Override
    public ArmazenamentoObjetos criarArmazenamento(int capacidadeGb) {
        return new ArmazenamentoBlob(gerarIdentificador(PREFIXO_ARMAZENAMENTO, SEQUENCIA_ARMAZENAMENTO), capacidadeGb);
    }

    @Override
    public FilaMensagens criarFila() {
        return new FilaServiceBus(gerarIdentificador(PREFIXO_FILA, SEQUENCIA_FILA));
    }

    private static String gerarIdentificador(String prefixo, AtomicInteger sequencia) {
        return prefixo + String.format("%03d", sequencia.incrementAndGet());
    }

    private static final class MaquinaVirtualAzure implements MaquinaVirtual {

        private final String identificador;

        private MaquinaVirtualAzure(String identificador) {
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

    private static final class ArmazenamentoBlob implements ArmazenamentoObjetos {

        private final String identificador;
        private final int capacidadeGb;

        private ArmazenamentoBlob(String identificador, int capacidadeGb) {
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

    private static final class FilaServiceBus implements FilaMensagens {

        private final String identificador;

        private FilaServiceBus(String identificador) {
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
