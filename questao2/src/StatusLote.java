public enum StatusLote {

    ABERTO("aberto"),
    VENDIDO("vendido"),
    NAO_VENDIDO("não vendido");

    private final String descricao;

    StatusLote(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
