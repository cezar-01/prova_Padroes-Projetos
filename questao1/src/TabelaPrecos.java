import java.math.BigDecimal;
import java.util.Objects;

public final class TabelaPrecos {

    public static final BigDecimal HORAS_POR_MES = new BigDecimal("730");

    private final BigDecimal custoHoraMaquina;
    private final BigDecimal custoGbMes;
    private final BigDecimal custoFixoFilaMes;

    public TabelaPrecos(BigDecimal custoHoraMaquina, BigDecimal custoGbMes, BigDecimal custoFixoFilaMes) {
        this.custoHoraMaquina = Objects.requireNonNull(custoHoraMaquina);
        this.custoGbMes = Objects.requireNonNull(custoGbMes);
        this.custoFixoFilaMes = Objects.requireNonNull(custoFixoFilaMes);
    }

    public BigDecimal estimarCustoMensal(int quantidadeMaquinas, int gbArmazenamento) {
        BigDecimal maquinas = custoHoraMaquina.multiply(HORAS_POR_MES)
                .multiply(BigDecimal.valueOf(quantidadeMaquinas));
        BigDecimal armazenamento = custoGbMes.multiply(BigDecimal.valueOf(gbArmazenamento));
        return maquinas.add(armazenamento).add(custoFixoFilaMes);
    }

    public BigDecimal getCustoHoraMaquina() {
        return custoHoraMaquina;
    }

    public BigDecimal getCustoGbMes() {
        return custoGbMes;
    }

    public BigDecimal getCustoFixoFilaMes() {
        return custoFixoFilaMes;
    }
}
