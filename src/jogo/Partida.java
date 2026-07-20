package jogo;

import java.io.Serializable;

public class Partida implements Serializable {
    private final Mapa mapa;

    public Partida(Mapa mapa) {
        this.mapa = mapa;
    }

    public Mapa getMapa() {
        return mapa;
    }
}
