package itens;

import java.io.Serializable;
import personagens.Personagem;

public abstract class Item implements Serializable {
    private int quantidade;
    private boolean consumivel;
    private int id;
    private String nome;
    private boolean isArma;

    public Item(boolean consumivel, int id, String nome, boolean isArma) {
        this.consumivel = consumivel;
        this.id = id;
        this.nome = nome;
        this.isArma = isArma;
        this.quantidade = 1;
    }
    
    public void receberItem ( ) {
        quantidade++;
    }
    
    public void gastarItem ( ) {
        quantidade--;
    }
    
    public int getQuantidade ( ) {
        return quantidade;
    }

    public boolean isConsumivel() {
        return consumivel;
    }
    
    public boolean isArma ( ) {
        return isArma;
    }

    public int getId() {
        return id;
    }
    
    public String getNome ( ) {
        return this.nome;
    }
    
    public abstract void usarItem ( Personagem alvo );
}