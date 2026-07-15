package personagens;

import interfaces.Movel;
import jogo.Mapa;

public abstract class Dinossauro extends Personagem {
    int id;

    public Dinossauro(String nome, int x, int y, char simbolo, int vida, int id) {
        super(nome, x, y, simbolo, vida);
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

}