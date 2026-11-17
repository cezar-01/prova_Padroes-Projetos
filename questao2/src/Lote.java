import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Lote {

    private static final BigDecimal FATOR_INCREMENTO_MINIMO = new BigDecimal("1.05");

    private final String nome;
    private final Categoria categoria;
    private final BigDecimal precoInicial;
    private final BigDecimal precoReserva;

    private final List<ObservadorLote> observadores = new ArrayList<>();
    private final List<Lance> lances = new ArrayList<>();

    private RegraComissao regraComissao;
    private StatusLote status = StatusLote.ABERTO;
    private BigDecimal comissaoCobrada;

    public Lote(String nome, Categoria categoria, BigDecimal precoInicial, BigDecimal precoReserva) {
        this.nome = Objects.requireNonNull(nome);
        this.categoria = Objects.requireNonNull(categoria);
        this.precoInicial = exigirPositivo(precoInicial, "preço inicial");
        this.precoReserva = exigirPositivo(precoReserva, "preço de reserva");
        this.regraComissao = categoria.getRegraPadrao();
    }

    public void acompanhar(ObservadorLote observador) {
        Objects.requireNonNull(observador);
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void deixarDeAcompanhar(ObservadorLote observador) {
        observadores.remove(observador);
    }

    private void notificarObservadores(Lance lance) {
        for (ObservadorLote observador : new ArrayList<>(observadores)) {
            if (observador != lance.getLicitante()) {
                observador.atualizar(this, lance);
            }
        }
    }

    public ResultadoLance receberLance(Licitante licitante, BigDecimal valor) {
        Objects.requireNonNull(licitante);
        Objects.requireNonNull(valor);
        if (status != StatusLote.ABERTO) {
            return ResultadoLance.recusado(String.format("Lance de %s (%s) no lote \"%s\": o lote está encerrado (%s).",
                    licitante.getNome(), Moeda.formatar(valor), nome, status.getDescricao()));
        }
        BigDecimal minimo = getLanceMinimo();
        if (valor.compareTo(minimo) < 0) {
            return ResultadoLance.recusado(String.format(
                    "Lance de %s (%s) no lote \"%s\": o lance deve ser pelo menos 5%% maior que o lance atual de %s, "
                            + "ou seja, no mínimo %s.",
                    licitante.getNome(), Moeda.formatar(valor), nome,
                    Moeda.formatar(getLanceAtual()), Moeda.formatar(minimo)));
        }
        Lance lance = new Lance(licitante, valor, LocalDateTime.now());
        lances.add(lance);
        notificarObservadores(lance);
        return ResultadoLance.aceito(String.format("Lance de %s (%s) no lote \"%s\".",
                licitante.getNome(), Moeda.formatar(valor), nome));
    }

    public BigDecimal getLanceAtual() {
        return getMaiorLance().map(Lance::getValor).orElse(precoInicial);
    }

    public BigDecimal getLanceMinimo() {
        return getLanceAtual().multiply(FATOR_INCREMENTO_MINIMO).setScale(2, RoundingMode.UP);
    }

    public Optional<Lance> getMaiorLance() {
        return lances.isEmpty() ? Optional.empty() : Optional.of(lances.get(lances.size() - 1));
    }

    public int getQuantidadeLances() {
        return lances.size();
    }

    public List<Lance> getLances() {
        return Collections.unmodifiableList(lances);
    }

    public void encerrar() {
        exigirAberto("encerrar");
        Optional<Lance> maior = getMaiorLance();
        if (maior.isPresent() && maior.get().getValor().compareTo(precoReserva) >= 0) {
            status = StatusLote.VENDIDO;
            comissaoCobrada = regraComissao.calcular(maior.get().getValor());
        } else {
            status = StatusLote.NAO_VENDIDO;
        }
    }

    public void setRegraComissao(RegraComissao novaRegra) {
        exigirAberto("trocar a regra de comissão");
        this.regraComissao = Objects.requireNonNull(novaRegra);
    }

    public RegraComissao getRegraComissao() {
        return regraComissao;
    }

    public boolean isVendido() {
        return status == StatusLote.VENDIDO;
    }

    public BigDecimal getValorVenda() {
        exigirVendido();
        return getMaiorLance().get().getValor();
    }

    public Licitante getComprador() {
        exigirVendido();
        return getMaiorLance().get().getLicitante();
    }

    public BigDecimal getComissaoCobrada() {
        exigirVendido();
        return comissaoCobrada;
    }

    public String getNome() {
        return nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public BigDecimal getPrecoInicial() {
        return precoInicial;
    }

    public BigDecimal getPrecoReserva() {
        return precoReserva;
    }

    public StatusLote getStatus() {
        return status;
    }

    private void exigirAberto(String operacao) {
        if (status != StatusLote.ABERTO) {
            throw new IllegalStateException("Não é possível " + operacao + ": o lote \"" + nome
                    + "\" já está encerrado (" + status.getDescricao() + ").");
        }
    }

    private void exigirVendido() {
        if (status != StatusLote.VENDIDO) {
            throw new IllegalStateException("O lote \"" + nome + "\" não foi vendido (" + status.getDescricao() + ").");
        }
    }

    private static BigDecimal exigirPositivo(BigDecimal valor, String campo) {
        Objects.requireNonNull(valor, campo);
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException("O " + campo + " deve ser maior que zero.");
        }
        return valor;
    }
}
