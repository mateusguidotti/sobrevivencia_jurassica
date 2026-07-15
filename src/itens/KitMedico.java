/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itens;

import personagens.Personagem;

/**
 *
 * @author gpedroso
 */
public class KitMedico extends Item {

    public KitMedico() {
        super(true, 1, "MedKit", false);
    }
    
    @Override
    public void usarItem ( Personagem alvo ) {
        alvo.curar(1);
    }
}
