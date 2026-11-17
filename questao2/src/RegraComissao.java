import java.math.BigDecimal;

public interface RegraComissao {

    BigDecimal calcular(BigDecimal valorVenda);

    String getDescricao();
}
