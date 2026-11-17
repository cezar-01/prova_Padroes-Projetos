import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class RegistroAuditoria {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");

    private final long sequencia;
    private final LocalDateTime dataHora;
    private final String nomeAmbiente;
    private final String provedor;
    private final ResultadoProvisionamento resultado;
    private final String motivo;

    public RegistroAuditoria(long sequencia, LocalDateTime dataHora, String nomeAmbiente, String provedor,
                             ResultadoProvisionamento resultado, String motivo) {
        this.sequencia = sequencia;
        this.dataHora = dataHora;
        this.nomeAmbiente = nomeAmbiente;
        this.provedor = provedor;
        this.resultado = resultado;
        this.motivo = motivo;
    }

    public long getSequencia() {
        return sequencia;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getNomeAmbiente() {
        return nomeAmbiente;
    }

    public String getProvedor() {
        return provedor;
    }

    public ResultadoProvisionamento getResultado() {
        return resultado;
    }

    public String getMotivo() {
        return motivo;
    }

    @Override
    public String toString() {
        String texto = String.format("#%d | %s | ambiente: %-16s | provedor: %-5s | %s",
                sequencia, FORMATO.format(dataHora), nomeAmbiente, provedor, resultado);
        return motivo == null ? texto : texto + " (motivo: " + motivo + ")";
    }
}
