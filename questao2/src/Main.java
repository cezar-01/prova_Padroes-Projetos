import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        Categoria arte = new Categoria("Arte", new ComissaoArte());
        Categoria veiculos = new Categoria("Veículos", new ComissaoVeiculos());
        Categoria imoveis = new Categoria("Imóveis", new ComissaoImoveis());
        Categoria eletronicos = new Categoria("Eletrônicos", new ComissaoEletronicos());

        demonstrarRegrasDeComissao(arte, veiculos, imoveis, eletronicos);

        Lote quadro = new Lote("Quadro 'Paisagem ao Entardecer'", arte,
                new BigDecimal("10000.00"), new BigDecimal("15000.00"));
        Lote notebook = new Lote("Notebook Gamer X15", eletronicos,
                new BigDecimal("4000.00"), new BigDecimal("9000.00"));

        Participante ana = new Participante("Ana");
        Participante bruno = new Participante("Bruno");
        Participante carla = new Participante("Carla");
        PainelLeiloeiro painel = new PainelLeiloeiro();
        AuditorLances auditor = new AuditorLances();

        quadro.acompanhar(ana);
        quadro.acompanhar(bruno);
        quadro.acompanhar(carla);
        quadro.acompanhar(painel);
        quadro.acompanhar(auditor);

        notebook.acompanhar(bruno);
        notebook.acompanhar(carla);
        notebook.acompanhar(painel);
        notebook.acompanhar(auditor);

        titulo("Pregão do lote \"" + quadro.getNome() + "\" (" + quadro.getCategoria().getNome()
                + ") - inicial " + Moeda.formatar(quadro.getPrecoInicial())
                + ", reserva " + Moeda.formatar(quadro.getPrecoReserva()));
        lance(ana, quadro, "10500.00");
        lance(bruno, quadro, "10800.00");
        lance(bruno, quadro, "12000.00");
        lance(carla, quadro, "12500.00");

        System.out.println("\n> Carla deixa de acompanhar o lote \"" + quadro.getNome() + "\"");
        quadro.deixarDeAcompanhar(carla);

        lance(ana, quadro, "13000.00");
        lance(bruno, quadro, "15500.00");

        titulo("Pregão do lote \"" + notebook.getNome() + "\" (" + notebook.getCategoria().getNome()
                + ") - inicial " + Moeda.formatar(notebook.getPrecoInicial())
                + ", reserva " + Moeda.formatar(notebook.getPrecoReserva()));
        lance(carla, notebook, "4300.00");
        lance(bruno, notebook, "4400.00");
        lance(bruno, notebook, "5000.00");
        lance(ana, notebook, "5200.00");

        titulo("Campanha: troca da regra de comissão do lote \"" + quadro.getNome() + "\"");
        System.out.println("Regra atual: " + quadro.getRegraComissao().getDescricao());
        quadro.setRegraComissao(eletronicos.getRegraPadrao());
        System.out.println("Nova regra : " + quadro.getRegraComissao().getDescricao());

        titulo("Encerramento dos lotes");
        encerrar(quadro);
        encerrar(notebook);

        System.out.println();
        System.out.println("> Bruno tenta dar lance após o encerramento");
        System.out.println("  " + bruno.darLance(notebook, new BigDecimal("9500.00")));

        titulo("Acerto do vendedor e fatura do comprador");
        new AcertoVendedor(quadro).imprimir();
        System.out.println();

        Fatura fatura = new FaturaVenda(quadro);
        fatura = new SeguroTransporte(fatura);
        fatura = new CertificadoAutenticidade(fatura);
        fatura = new EmbalagemEspecial(fatura);
        fatura = new EmbalagemEspecial(fatura);
        fatura = new EntregaExpressa(fatura);
        ImpressoraFatura.imprimir(fatura);

        System.out.println();
        System.out.println("(Mesma impressão para uma venda sem serviços adicionais - RNF04)");
        ImpressoraFatura.imprimir(new FaturaVenda(quadro));

        titulo("Auditoria");
        auditor.imprimirRegistros();
        System.out.println("Total de lances recebidos pelo painel: " + painel.getTotalLancesRecebidos());
    }

    private static void lance(Participante participante, Lote lote, String valor) {
        BigDecimal quantia = new BigDecimal(valor);
        System.out.println();
        System.out.println("> " + participante.getNome() + " oferece " + Moeda.formatar(quantia)
                + " no lote \"" + lote.getNome() + "\" (mínimo: " + Moeda.formatar(lote.getLanceMinimo()) + ")");
        ResultadoLance resultado = participante.darLance(lote, quantia);
        System.out.println("  " + resultado);
    }

    private static void encerrar(Lote lote) {
        lote.encerrar();
        String detalhe = lote.isVendido()
                ? "VENDIDO para " + lote.getComprador().getNome() + " por " + Moeda.formatar(lote.getValorVenda())
                : "NÃO VENDIDO (maior lance " + Moeda.formatar(lote.getLanceAtual())
                        + " abaixo da reserva de " + Moeda.formatar(lote.getPrecoReserva()) + ")";
        System.out.println("Lote \"" + lote.getNome() + "\": " + detalhe);
    }

    private static void demonstrarRegrasDeComissao(Categoria... categorias) {
        titulo("Regras de comissão por categoria (RF06)");
        BigDecimal[] valores = {new BigDecimal("300.00"), new BigDecimal("15500.00"), new BigDecimal("100000.00")};
        for (Categoria categoria : categorias) {
            StringBuilder linha = new StringBuilder(String.format("%-48s", categoria.getRegraPadrao().getDescricao()));
            for (BigDecimal valor : valores) {
                linha.append(String.format(" | %s -> %s", Moeda.formatar(valor),
                        Moeda.formatar(categoria.getRegraPadrao().calcular(valor))));
            }
            System.out.println(linha);
        }
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("################ " + texto + " ################");
    }
}
