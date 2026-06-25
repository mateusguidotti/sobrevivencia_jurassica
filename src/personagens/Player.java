package personagens;

import jogo.Inventario;
import jogo.Dado;
import jogo.Mapa;
import interfaces.Movel;

public class Player extends Personagem implements Movel{
    private final int percepcao;
    private final Inventario inventario;
    
    public Player(String nome, int x, int y, int vida, int percepcao){
        super(nome, x, y, 'P', vida);
        this.percepcao = percepcao;
        inventario = new Inventario();
    }  
    
    @Override
    public void atacar(Personagem alvo, int dano){
        alvo.tomarDano(dano);
    }
    
    @Override
    public void mover(int x, int y, Mapa mapa){
        if(x == super.getX() && y == super.getY()){
            System.out.println("Vocês está tentando mover para a posição em que está");
        }else{
            this.setPosition(x, y);
        }
        
        
        
    }
    
    public int getPercepcao(){
        return this.percepcao;
    }
    
    public void pegarBastao(){
        inventario.setBastao(true);
    }
    
    public boolean temBastao(){
        return this.inventario.temBastao();
    }
    
    public boolean esquivar(Dado dado){
        int testePercepcao = dado.rolar(3);
        return testePercepcao <= this.percepcao;
    }
    
    @Override
    public String toString(){
        return(
                super.toString() + "\n" +
                "Percepção: " + this.percepcao
        );
    }
}
