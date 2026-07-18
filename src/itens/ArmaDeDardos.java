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
public class ArmaDeDardos extends Arma {

    public ArmaDeDardos() {
        super(true, 3, "Arma de dardos");
    }

    @Override
    public void usarItem(Personagem alvo) {
        System.out.println("Usou arma de dardos");
        alvo.tomarDano(2);
    }
    
    @Override
    public void usarArma ( Dinossauro alvo, int dado ) {
        if ( alvo.podeSerAtacadoComDardos() ) {
        usarItem ( alvo );
        } else {
            System.out.println("Inimigo não pode ser ferido com dardos");
        }
    }
    
}
