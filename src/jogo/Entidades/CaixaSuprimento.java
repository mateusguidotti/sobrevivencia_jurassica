package jogo.Entidades;

import itens.Item;
import jogo.Entidade;


public class CaixaSuprimento extends Entidade {


    private final Item conteudo;
    private final boolean compsognatoSurpresa;

    public CaixaSuprimento(int x, int y, Item conteudo){
        super("Caixa de Suprimentos", x, y, 'X', "caixa_suprimentos.png");
        this.conteudo = conteudo;
        if ( conteudo.getId() == 1 ) {
            compsognatoSurpresa = true;
        } else {
            compsognatoSurpresa = false;
        }
    }


    public Item getConteudo(){
        return conteudo;
    }

    public boolean isCompsognatoSurpresa(){
        return compsognatoSurpresa;
    }
}
