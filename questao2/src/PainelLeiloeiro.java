public class PainelLeiloeiro implements ObservadorLote {

    private int totalLancesRecebidos;

    @Override
    public void atualizar(Lote lote, Lance lance) {
        totalLancesRecebidos++;
        System.out.println("    [Painel do leiloeiro] \"" + lote.getNome() + "\": maior lance "
                + Moeda.formatar(lote.getLanceAtual()) + " (" + lance.getLicitante().getNome() + ")"
                + " | lances no lote: " + lote.getQuantidadeLances()
                + " | total de lances recebidos no painel: " + totalLancesRecebidos);
    }

    public int getTotalLancesRecebidos() {
        return totalLancesRecebidos;
    }
}
