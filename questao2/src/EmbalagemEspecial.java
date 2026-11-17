import java.math.BigDecimal;

public class EmbalagemEspecial extends ServicoAdicional {

    private static final BigDecimal CUSTO = new BigDecimal("180.00");

    public EmbalagemEspecial(Fatura fatura) {
        super(fatura);
    }

    @Override
    protected String getDescricaoServico() {
        return "Embalagem especial";
    }

    @Override
    protected BigDecimal calcularCusto() {
        return CUSTO;
    }
}
