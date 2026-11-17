import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public final class Provisionador {

    private final FabricaRecursosNuvem fabrica;

    public Provisionador(FabricaRecursosNuvem fabrica) {
        this.fabrica = Objects.requireNonNull(fabrica, "a fábrica do provedor é obrigatória");
    }

    public Optional<Ambiente> provisionar(SolicitacaoAmbiente solicitacao) {
        Configuracao configuracao = Configuracao.getInstancia();
        HistoricoAuditoria historico = HistoricoAuditoria.getInstancia();
        String provedor = fabrica.getNomeProvedor();

        BigDecimal custoEstimado = fabrica.getTabelaPrecos()
                .estimarCustoMensal(solicitacao.getQuantidadeMaquinas(), solicitacao.getGbArmazenamento());
        BigDecimal orcamento = configuracao.getOrcamentoMensalMaximo();

        if (custoEstimado.compareTo(orcamento) > 0) {
            String motivo = "custo estimado de " + Moeda.formatar(custoEstimado)
                    + " ultrapassa o orçamento mensal máximo de " + Moeda.formatar(orcamento);
            historico.registrarRecusado(solicitacao.getNomeAmbiente(), provedor, motivo);
            System.out.println("==================== PROVISIONAMENTO RECUSADO ====================\n"
                    + "Ambiente : " + solicitacao.getNomeAmbiente() + '\n'
                    + "Provedor : " + provedor + '\n'
                    + "Motivo   : " + motivo + " (nenhum recurso foi criado)");
            return Optional.empty();
        }

        Ambiente ambiente = new Ambiente(solicitacao.getNomeAmbiente(), configuracao.getRegiaoPadrao(),
                fabrica, solicitacao.getQuantidadeMaquinas(), solicitacao.getGbArmazenamento());
        historico.registrarAceito(ambiente.getNome(), provedor);
        System.out.println(ambiente.gerarResumo());
        return Optional.of(ambiente);
    }
}
