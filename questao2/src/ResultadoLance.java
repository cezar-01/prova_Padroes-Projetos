public final class ResultadoLance {

    private final boolean aceito;
    private final String mensagem;

    private ResultadoLance(boolean aceito, String mensagem) {
        this.aceito = aceito;
        this.mensagem = mensagem;
    }

    public static ResultadoLance aceito(String mensagem) {
        return new ResultadoLance(true, mensagem);
    }

    public static ResultadoLance recusado(String mensagem) {
        return new ResultadoLance(false, mensagem);
    }

    public boolean isAceito() {
        return aceito;
    }

    public String getMensagem() {
        return mensagem;
    }

    @Override
    public String toString() {
        return (aceito ? "[ACEITO] " : "[RECUSADO] ") + mensagem;
    }
}
