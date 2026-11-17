import java.util.Objects;

public final class SolicitacaoAmbiente {

    private final String nomeAmbiente;
    private final String provedor;
    private final int quantidadeMaquinas;
    private final int gbArmazenamento;

    public SolicitacaoAmbiente(String nomeAmbiente, String provedor, int quantidadeMaquinas, int gbArmazenamento) {
        this.nomeAmbiente = Objects.requireNonNull(nomeAmbiente, "nome do ambiente é obrigatório");
        this.provedor = Objects.requireNonNull(provedor, "provedor é obrigatório");
        if (quantidadeMaquinas < 1) {
            throw new IllegalArgumentException("O ambiente precisa de ao menos uma máquina virtual.");
        }
        if (gbArmazenamento < 0) {
            throw new IllegalArgumentException("A quantidade de GB não pode ser negativa.");
        }
        this.quantidadeMaquinas = quantidadeMaquinas;
        this.gbArmazenamento = gbArmazenamento;
    }

    public String getNomeAmbiente() {
        return nomeAmbiente;
    }

    public String getProvedor() {
        return provedor;
    }

    public int getQuantidadeMaquinas() {
        return quantidadeMaquinas;
    }

    public int getGbArmazenamento() {
        return gbArmazenamento;
    }
}
