import java.math.BigDecimal;
import java.util.List;

public interface Fatura {

    String getNomeLote();

    String getComprador();

    BigDecimal getValorVenda();

    List<ItemFatura> getServicos();

    BigDecimal getTotal();
}
