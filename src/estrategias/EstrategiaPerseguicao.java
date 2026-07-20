/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estrategias;

import jogo.Mapa;
import personagens.Dinossauro;
import personagens.Player;
import interfaces.EstrategiaMovimento;

/**
 *
 * @author dudur
 */
public class EstrategiaPerseguicao implements EstrategiaMovimento {

    @Override
    public int[] calcularProximoPasso(Dinossauro dino, Player player, Mapa mapa) {
        int dx = 0, dy = 0;

        int diffX = player.getX() - dino.getX();
        int diffY = player.getY() - dino.getY();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            dx = Integer.signum(diffX);
        } else if (diffY != 0) {
            dy = Integer.signum(diffY);
        } else if (diffX != 0) {
            dx = Integer.signum(diffX);
        }

        int novoX = dino.getX() + dx;
        int novoY = dino.getY() + dy;

        if (novoX < 0 || novoX >= mapa.getTamanho() || novoY < 0 || novoY >= mapa.getTamanho()) {
            return null;
        }
        return new int[]{novoX, novoY};
    }
}