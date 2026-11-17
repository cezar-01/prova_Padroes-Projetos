public final class ImpressoraFatura {

    private ImpressoraFatura() {
    }

    public static void imprimir(Fatura fatura) {
        System.out.println("=============== FATURA DO COMPRADOR ===============");
        System.out.println("Lote ..............: " + fatura.getNomeLote());
        System.out.println("Comprador .........: " + fatura.getComprador());
        System.out.println("Valor de venda ....: " + Moeda.formatar(fatura.getValorVenda()));
        System.out.println("Serviços adicionais: " + fatura.getServicos().size());
        for (ItemFatura servico : fatura.getServicos()) {
            System.out.println(String.format("  - %-46s %s", servico.getDescricao(), Moeda.formatar(servico.getValor())));
        }
        System.out.println("Total a pagar .....: " + Moeda.formatar(fatura.getTotal()));
    }
}
