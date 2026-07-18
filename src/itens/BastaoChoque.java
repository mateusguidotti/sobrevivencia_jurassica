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
public class BastaoChoque extends Arma {

    public BastaoChoque() {
        super(false, 2, "Bastão de choque");
    }
    @Override
    public void usarItem ( Personagem alvo ) {
        alvo.tomarDano(1);
    }
    @Override
    public void usarArma ( Dinossauro alvo, int dado ) {
        System.out.println("Usou bastão de choque");
        if ( dado == 1 ) {
            System.out.println("Errou ");  
        } else if ( dado < 5 ) {
            usarItem ( alvo );
        } else {
            usarItem ( alvo );
            usarItem ( alvo );
        }
    }
}
