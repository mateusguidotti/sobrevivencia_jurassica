/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import jogo.Mapa;
import personagens.Dinossauro;
import personagens.Player;

/**
 *
 * @author dudur
 */
public interface EstrategiaMovimento {
    int[] calcularProximoPasso(Dinossauro dino, Player player, Mapa mapa);
}
