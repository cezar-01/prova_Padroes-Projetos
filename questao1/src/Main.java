import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class Main {

    private static final Map<String, Supplier<FabricaRecursosNuvem>> PROVEDORES = Map.of(
            "Aws", FabricaAws::new,
            "Azure", FabricaAzure::new);

    public static void main(String[] args) throws Exception {
        List<SolicitacaoAmbiente> solicitacoes = List.of(
                new SolicitacaoAmbiente("pagamentos-dev", "Aws", 2, 100),
                new SolicitacaoAmbiente("pagamentos-prod", "Aws", 4, 500),
                new SolicitacaoAmbiente("relatorios-hml", "Azure", 3, 200),
                new SolicitacaoAmbiente("analytics-prod", "Azure", 10, 1000)
        );

        System.out.println("Disparando " + solicitacoes.size() + " provisionamentos simultâneos...\n");

        AtomicInteger numeroThread = new AtomicInteger();
        ExecutorService executor = Executors.newFixedThreadPool(solicitacoes.size(),
                tarefa -> new Thread(tarefa, "provisionamento-" + numeroThread.incrementAndGet()));
        CountDownLatch largada = new CountDownLatch(1);

        List<Future<?>> execucoes = new ArrayList<>();
        try {
            for (SolicitacaoAmbiente solicitacao : solicitacoes) {
                execucoes.add(executor.submit(() -> {
                    largada.await();
                    new Provisionador(escolherProvedor(solicitacao.getProvedor())).provisionar(solicitacao);
                    return null;
                }));
            }
            largada.countDown();
            for (Future<?> execucao : execucoes) {
                execucao.get();
            }
        } finally {
            executor.shutdown();
        }

        System.out.println();
        HistoricoAuditoria.getInstancia().imprimir();
        System.out.println("Registros esperados: " + solicitacoes.size()
                + " | registrados: " + HistoricoAuditoria.getInstancia().getQuantidade());
        System.out.println("Leituras do config.properties: " + Configuracao.getQuantidadeLeituras());
    }

    private static FabricaRecursosNuvem escolherProvedor(String nomeProvedor) {
        Supplier<FabricaRecursosNuvem> fabrica = PROVEDORES.get(nomeProvedor);
        if (fabrica == null) {
            throw new IllegalArgumentException("Provedor não suportado: " + nomeProvedor);
        }
        return fabrica.get();
    }
}
