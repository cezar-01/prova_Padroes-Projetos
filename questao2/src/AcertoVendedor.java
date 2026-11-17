import java.math.BigDecimal;

public final class AcertoVendedor {

    private final String nomeLote;
    private final String regraAplicada;
    private final BigDecimal valorVenda;
    private final BigDecimal comissao;

    public AcertoVendedor(Lote lote) {
        if (!lote.isVendido()) {
            throw new IllegalStateException("Só há acerto para lotes vendidos: \"" + lote.getNome() + "\".");
        }
        this.nomeLote = lote.getNome();
        this.regraAplicada = lote.getRegraComissao().getDescricao();
        this.valorVenda = lote.getValorVenda();
        this.comissao = lote.getComissaoCobrada();
    }

    public BigDecimal getValorLiquido() {
        return valorVenda.subtract(comissao);
    }

    public void imprimir() {
        System.out.println("=============== ACERTO DO VENDEDOR ===============");
        System.out.println("Lote ...................: " + nomeLote);
        System.out.println("Valor de venda .........: " + Moeda.formatar(valorVenda));
        System.out.println("Comissão cobrada .......: " + Moeda.formatar(comissao) + " (" + regraAplicada + ")");
        System.out.println("Valor líquido a repassar: " + Moeda.formatar(getValorLiquido()));
    }
}
