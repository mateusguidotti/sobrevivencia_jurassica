package gui;

import jogo.Mapa;

public class JogoFinal {

    
    public static void main(String[] args){
        Mapa mapa = new Mapa(10);
        mapa.gerar();
        FramePrincipal jogo = new FramePrincipal(mapa, mapa.getPlayer());
    }
}