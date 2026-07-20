/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estrategias;

import java.util.Random;
import jogo.Mapa;
import personagens.Dinossauro;
import personagens.Player;
import interfaces.EstrategiaMovimento;
        
/**
 *
 * @author dudur
 */
public class EstrategiaAleatoria implements EstrategiaMovimento {

    private static final int[][] DIRECOES = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private final Random random = new Random();

    @Override
    public int[] calcularProximoPasso(Dinossauro dino, Player player, Mapa mapa) {
        int[] direcao = DIRECOES[random.nextInt(DIRECOES.length)];

        int novoX = dino.getX() + direcao[0];
        int novoY = dino.getY() + direcao[1];

        if (novoX < 0 || novoX >= mapa.getTamanho() || novoY < 0 || novoY >= mapa.getTamanho()) {
            return null;
        }
        return new int[]{novoX, novoY};
    }
}
