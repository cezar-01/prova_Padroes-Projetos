import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public final class FaturaVenda implements Fatura {

    private final String nomeLote;
    private final String comprador;
    private final BigDecimal valorVenda;

    public FaturaVenda(Lote lote) {
        if (!lote.isVendido()) {
            throw new IllegalStateException("Só há fatura para lotes vendidos: \"" + lote.getNome() + "\".");
        }
        this.nomeLote = lote.getNome();
        this.comprador = lote.getComprador().getNome();
        this.valorVenda = lote.getValorVenda();
    }

    @Override
    public String getNomeLote() {
        return nomeLote;
    }

    @Override
    public String getComprador() {
        return comprador;
    }

    @Override
    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    @Override
    public List<ItemFatura> getServicos() {
        return Collections.emptyList();
    }

    @Override
    public BigDecimal getTotal() {
        return valorVenda;
    }
}
