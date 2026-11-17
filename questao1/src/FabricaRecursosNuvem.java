public interface FabricaRecursosNuvem {

    String getNomeProvedor();

    TabelaPrecos getTabelaPrecos();

    MaquinaVirtual criarMaquinaVirtual();

    ArmazenamentoObjetos criarArmazenamento(int capacidadeGb);

    FilaMensagens criarFila();
}
