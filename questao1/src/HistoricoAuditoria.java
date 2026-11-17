import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HistoricoAuditoria {

    private static final HistoricoAuditoria INSTANCIA = new HistoricoAuditoria();

    private final List<RegistroAuditoria> registros = new ArrayList<>();
    private long proximaSequencia = 1;

    private HistoricoAuditoria() {
    }

    public static HistoricoAuditoria getInstancia() {
        return INSTANCIA;
    }

    public void registrarAceito(String nomeAmbiente, String provedor) {
        registrar(nomeAmbiente, provedor, ResultadoProvisionamento.ACEITO, null);
    }

    public void registrarRecusado(String nomeAmbiente, String provedor, String motivo) {
        registrar(nomeAmbiente, provedor, ResultadoProvisionamento.RECUSADO, motivo);
    }

    private synchronized void registrar(String nomeAmbiente, String provedor,
                                        ResultadoProvisionamento resultado, String motivo) {
        registros.add(new RegistroAuditoria(proximaSequencia++, LocalDateTime.now(),
                nomeAmbiente, provedor, resultado, motivo));
    }

    public synchronized List<RegistroAuditoria> getRegistros() {
        return Collections.unmodifiableList(new ArrayList<>(registros));
    }

    public synchronized int getQuantidade() {
        return registros.size();
    }

    public void imprimir() {
        List<RegistroAuditoria> copia = getRegistros();
        StringBuilder sb = new StringBuilder();
        sb.append("==================== HISTÓRICO DE AUDITORIA ====================\n");
        for (RegistroAuditoria registro : copia) {
            sb.append(registro).append('\n');
        }
        sb.append("Total de registros: ").append(copia.size());
        System.out.println(sb);
    }
}
