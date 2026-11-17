import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuditorLances implements ObservadorLote {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");

    private final List<String> registros = new ArrayList<>();

    @Override
    public void atualizar(Lote lote, Lance lance) {
        String registro = FORMATO.format(lance.getDataHora()) + " | lote \"" + lote.getNome() + "\" | "
                + lance.getLicitante().getNome() + " | " + Moeda.formatar(lance.getValor());
        registros.add(registro);
        System.out.println("    [Auditor] Registro #" + registros.size() + ": " + registro);
    }

    public List<String> getRegistros() {
        return Collections.unmodifiableList(registros);
    }

    public void imprimirRegistros() {
        System.out.println("===== Registros do auditor (" + registros.size() + " lances aceitos) =====");
        for (int i = 0; i < registros.size(); i++) {
            System.out.println("#" + (i + 1) + " " + registros.get(i));
        }
    }
}
