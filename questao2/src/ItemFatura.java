import java.math.BigDecimal;
import java.util.Objects;

public final class ItemFatura {

    private final String descricao;
    private final BigDecimal valor;

    public ItemFatura(String descricao, BigDecimal valor) {
        this.descricao = Objects.requireNonNull(descricao);
        this.valor = Objects.requireNonNull(valor);
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
