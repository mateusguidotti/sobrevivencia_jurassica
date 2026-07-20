package interfaces;

import java.io.Serializable;
import jogo.Mapa;
import personagens.Dinossauro;
import personagens.Player;

public interface EstrategiaMovimento extends Serializable {
    int[] calcularProximoPasso(Dinossauro dino, Player player, Mapa mapa);
}