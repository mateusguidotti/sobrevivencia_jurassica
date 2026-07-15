package personagens;

import jogo.Inventario;
import jogo.Dado;
import jogo.Mapa;
import interfaces.Movel;

public class Player extends Personagem implements Movel{
    private final int percepcao;
    private final Inventario inventario;
    
    public Player(int x, int y, int vida, int percepcao){
        super("Player", x, y, 'P', vida);
        this.percepcao = percepcao;
        inventario = new Inventario();
    }  
    

    
    @Override
    public int getPassosMovimento ( ) {
        return 1;
    }
    
    public void atacarComPunhos ( Dinossauro alvo, int dado ) {
        if ( alvo.podeSerAtacadoSemArma() ) {
            if ( dado <= 2 ) {
                System.out.println("Errou ");
            } else if ( dado <= 5 ) {
                alvo.tomarDano(1);
                System.out.println("Acertou ");
            } else {
                alvo.tomarDano(2);
                System.out.println("Acerto crítico! ");
            }
        }
    }
    
    @Override
    public void mover(int x, int y, Mapa mapa){
            mapa.setCelula(this.getX(), this.getY(), null);
            this.setPosition(x, y);
            mapa.setCelula(x, y, this);
    }
    
    public int getPercepcao(){
        return this.percepcao;
    }

    public Inventario getInventario() {
        return inventario;
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
