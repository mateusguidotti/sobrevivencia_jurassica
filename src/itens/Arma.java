/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itens;

import personagens.Dinossauro;
import personagens.Personagem;

/**
 *
 * @author gpedroso
 */
public abstract class Arma extends Item {
    
    public Arma(boolean consumivel, int id, String nome) {
        super(consumivel, id, nome, true);
    }
    
    public void usarItem ( Personagem alvo ) {
        alvo.tomarDano(1);
    }
    
    public abstract void usarArma ( Dinossauro alvo, int dado );
    
}
