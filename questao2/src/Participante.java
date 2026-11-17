import java.math.BigDecimal;
import java.util.Objects;

public class Participante implements Licitante, ObservadorLote {

    private final String nome;

    public Participante(String nome) {
        this.nome = Objects.requireNonNull(nome);
    }

    public ResultadoLance darLance(Lote lote, BigDecimal valor) {
        return lote.receberLance(this, valor);
    }

    @Override
    public void atualizar(Lote lote, Lance lance) {
        System.out.println("    [" + nome + "] Novo lance no lote \"" + lote.getNome() + "\": "
                + Moeda.formatar(lance.getValor()));
    }

    @Override
    public String getNome() {
        return nome;
    }
}
