package jogo;

public class Entidade {
    private String nome;
    private int x;
    private int y;
    private char simbolo;
    
    public Entidade(String nome, int x, int y, char simbolo){
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.simbolo = simbolo;
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
    
    
    @Override
    public String toString(){
        return(
        "Nome: " + this.nome + "\n" +
        "Posição: (" + this.x + "," + this.y + ")\n" +
        "Simbolo: " + this.simbolo
        );
    }
}
