/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jogo.Entidades;

import jogo.Entidade;

/**
 *
 * @author gpedroso
 */
public class Parede extends Entidade {
    
    public Parede(int x, int y) {
        super("Parede", x, y, '█', "wall.png");
    }
    
}
