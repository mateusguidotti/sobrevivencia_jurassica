package jogo;

import java.awt.Image;
import java.io.Serializable;

public class Entidade implements Serializable {
    private String nome;
    private int x;
    private int y;
    private char simbolo;
    private String imagem;
    
    public Entidade(String nome, int x, int y, char simbolo, String imagem){
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.simbolo = simbolo;
        this.imagem = imagem;
    }
    
    public String getNome(){
        return this.nome;
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public char getSimbolo(){
        return this.simbolo;
    }
    
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
    
    
    
    @Override
    public String toString(){
        return(
        "Nome: " + this.nome + "\n" +
        "Posição: (" + this.x + "," + this.y + ")\n" +
        "Simbolo: " + this.simbolo
        );
    }
}
