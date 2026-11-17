import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Lance {

    private final Licitante licitante;
    private final BigDecimal valor;
    private final LocalDateTime dataHora;

    public Lance(Licitante licitante, BigDecimal valor, LocalDateTime dataHora) {
        this.licitante = Objects.requireNonNull(licitante);
        this.valor = Objects.requireNonNull(valor);
        this.dataHora = Objects.requireNonNull(dataHora);
    }

    public Licitante getLicitante() {
        return licitante;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}
