import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Ambiente {

    private final String nome;
    private final String provedor;
    private final String regiao;
    private final List<MaquinaVirtual> maquinas;
    private final ArmazenamentoObjetos armazenamento;
    private final FilaMensagens fila;

    public Ambiente(String nome, String regiao, FabricaRecursosNuvem fabrica,
                    int quantidadeMaquinas, int gbArmazenamento) {
        Objects.requireNonNull(fabrica, "a fábrica do provedor é obrigatória");
        this.nome = Objects.requireNonNull(nome);
        this.regiao = Objects.requireNonNull(regiao);
        this.provedor = fabrica.getNomeProvedor();

        List<MaquinaVirtual> criadas = new ArrayList<>();
        for (int i = 0; i < quantidadeMaquinas; i++) {
            criadas.add(fabrica.criarMaquinaVirtual());
        }
        this.maquinas = Collections.unmodifiableList(criadas);
        this.armazenamento = fabrica.criarArmazenamento(gbArmazenamento);
        this.fila = fabrica.criarFila();
    }

    public BigDecimal getCustoMensalEstimado() {
        BigDecimal total = BigDecimal.ZERO;
        for (MaquinaVirtual maquina : maquinas) {
            total = total.add(maquina.getCustoMensal());
        }
        return total.add(armazenamento.getCustoMensal()).add(fila.getCustoMensal());
    }

    public String gerarResumo() {
        StringBuilder sb = new StringBuilder();
        sb.append("==================== AMBIENTE PROVISIONADO ====================\n");
        sb.append("Ambiente : ").append(nome).append('\n');
        sb.append("Provedor : ").append(provedor).append('\n');
        sb.append("Região   : ").append(regiao).append('\n');
        sb.append("Recursos :\n");
        for (MaquinaVirtual maquina : maquinas) {
            sb.append(String.format("  - Máquina virtual   %-12s %s/h x %s h = %s%n",
                    maquina.getIdentificador(), Moeda.formatarUnitario(maquina.getCustoPorHora()),
                    TabelaPrecos.HORAS_POR_MES, Moeda.formatar(maquina.getCustoMensal())));
        }
        sb.append(String.format("  - Armazenamento     %-12s %d GB x %s/GB = %s%n",
                armazenamento.getIdentificador(), armazenamento.getCapacidadeGb(),
                Moeda.formatarUnitario(armazenamento.getCustoPorGbMes()),
                Moeda.formatar(armazenamento.getCustoMensal())));
        sb.append(String.format("  - Fila de mensagens %-12s custo fixo = %s%n",
                fila.getIdentificador(), Moeda.formatar(fila.getCustoMensal())));
        sb.append("Custo mensal estimado: ").append(Moeda.formatar(getCustoMensalEstimado()));
        return sb.toString();
    }

    public String getNome() {
        return nome;
    }

    public String getProvedor() {
        return provedor;
    }

    public String getRegiao() {
        return regiao;
    }

    public List<MaquinaVirtual> getMaquinas() {
        return maquinas;
    }

    public ArmazenamentoObjetos getArmazenamento() {
        return armazenamento;
    }

    public FilaMensagens getFila() {
        return fila;
    }
}
