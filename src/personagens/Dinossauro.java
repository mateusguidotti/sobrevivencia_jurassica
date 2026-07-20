package personagens;

import interfaces.EstrategiaMovimento;

public abstract class Dinossauro extends Personagem {
    int id;
    private EstrategiaMovimento estrategia;

    public Dinossauro(String nome, int x, int y, char simbolo, int vida, int id, String imagem) {
        super(nome, x, y, simbolo, vida, imagem);
        this.id = id;
    }

    public void atacar(Personagem alvo, int dano) {
        alvo.tomarDano(dano);
    }

    public boolean podeSerAtacadoSemArma() {
        return true;
    }

    public boolean podeSerAtacadoComDardos() {
        return true;
    }

    public boolean perseguePersonagem() {
        return false;
    }

    protected void setEstrategia(EstrategiaMovimento estrategia) {
        this.estrategia = estrategia;
    }

    public EstrategiaMovimento getEstrategia() {
        return estrategia;
    }

    public long getIntervaloMovimentoMs() {
        return 10_000;
    }
}