import java.util.Objects;

public final class Categoria {

    private final String nome;
    private final RegraComissao regraPadrao;

    public Categoria(String nome, RegraComissao regraPadrao) {
        this.nome = Objects.requireNonNull(nome);
        this.regraPadrao = Objects.requireNonNull(regraPadrao);
    }

    public String getNome() {
        return nome;
    }

    public RegraComissao getRegraPadrao() {
        return regraPadrao;
    }
}
