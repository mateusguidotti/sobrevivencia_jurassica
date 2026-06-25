package jogo;

public class Inventario {
    private boolean bastao;
    private boolean armaDeDardos;
    
    public Inventario(){
        bastao = false;
        armaDeDardos = false;
    }
    
    public void setBastao(boolean valor){
        bastao = valor;
    }
    
    public boolean temBastao(){
        return bastao;
    }
}
