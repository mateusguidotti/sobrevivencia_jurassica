package interfaces;

public interface CombateIO {

    void log(String mensagem);

    boolean desejaFugir();

    int escolherArma(int numArmas, String[] nomesArmas);

    void atualizarDinoStats(String nome, int vidaAtual, int vidaMaxima);

    void limparDinoStats();
}
