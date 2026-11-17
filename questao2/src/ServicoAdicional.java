import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class ServicoAdicional implements Fatura {

    private final Fatura faturaDecorada;

    protected ServicoAdicional(Fatura faturaDecorada) {
        this.faturaDecorada = Objects.requireNonNull(faturaDecorada, "o serviço precisa decorar uma fatura");
    }

    protected abstract String getDescricaoServico();

    protected abstract BigDecimal calcularCusto();

    @Override
    public String getNomeLote() {
        return faturaDecorada.getNomeLote();
    }

    @Override
    public String getComprador() {
        return faturaDecorada.getComprador();
    }

    @Override
    public BigDecimal getValorVenda() {
        return faturaDecorada.getValorVenda();
    }

    @Override
    public List<ItemFatura> getServicos() {
        List<ItemFatura> servicos = new ArrayList<>(faturaDecorada.getServicos());
        servicos.add(new ItemFatura(getDescricaoServico(), calcularCusto()));
        return Collections.unmodifiableList(servicos);
    }

    @Override
    public BigDecimal getTotal() {
        return faturaDecorada.getTotal().add(calcularCusto());
    }
}
