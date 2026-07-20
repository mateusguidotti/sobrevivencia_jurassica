package jogo.Entidades;

import itens.Item;
import jogo.Entidade;

/**
 * Caixa de suprimentos espalhada pelo mapa.
 * Conforme especificação: das 4 caixas, uma contém Kit Médico, uma contém
 * Bastão Elétrico, e duas contêm Arma de Dardos (sendo que uma destas
 * esconde, como surpresa, um Compsognato).
 */
public class CaixaSuprimento extends Entidade {


    private final Item conteudo;
    private final boolean compsognatoSurpresa;

    public CaixaSuprimento(int x, int y, Item conteudo){
        super("Caixa de Suprimentos", x, y, 'X');
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
